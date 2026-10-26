package me.capcom.smsgateway.modules.incoming.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import me.capcom.smsgateway.modules.incoming.db.IncomingMessage
import me.capcom.smsgateway.modules.incoming.db.IncomingMessageTotals
import me.capcom.smsgateway.modules.incoming.db.IncomingMessageType
import me.capcom.smsgateway.modules.incoming.repositories.IncomingMessagesRepository

class IncomingMessagesListViewModel(private val repository: IncomingMessagesRepository) : ViewModel() {
    val totals: LiveData<IncomingMessageTotals> = repository.totals

    private val queryParams = MutableLiveData<Pair<Int, IncomingMessageType?>>(CHUNK_SIZE to null)
    val filter: LiveData<IncomingMessageType?> = queryParams.map { it.second }

    private val _messages = MediatorLiveData<List<IncomingMessage>>()
    val messages: LiveData<List<IncomingMessage>> = _messages

    private var isLoading = false
    private var hasMore = true

    init {
        _messages.addSource(
            queryParams.switchMap { (l, type) ->
                repository.selectLast(l, type)
            }
        ) {
            _messages.value = it
            hasMore = it.size >= (queryParams.value?.first ?: CHUNK_SIZE)
            isLoading = false
        }
    }

    fun loadMore(index: Int = 0) {
        val currentLimit = queryParams.value?.first ?: 0
        if (currentLimit >= index + CHUNK_SIZE || isLoading || !hasMore) return

        isLoading = true
        queryParams.value = (currentLimit + CHUNK_SIZE) to queryParams.value?.second
    }

    fun setFilter(type: IncomingMessageType?) {
        val current = queryParams.value
        if (current?.second == type) return

        queryParams.value = CHUNK_SIZE to type
        hasMore = true
    }

    companion object {
        private const val CHUNK_SIZE = 50
    }
}
