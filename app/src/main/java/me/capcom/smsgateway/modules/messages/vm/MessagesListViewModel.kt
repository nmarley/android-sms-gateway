package me.capcom.smsgateway.modules.messages.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import me.capcom.smsgateway.data.entities.Message
import me.capcom.smsgateway.data.entities.MessagesTotals
import me.capcom.smsgateway.domain.ProcessingState
import me.capcom.smsgateway.modules.messages.MessagesRepository

class MessagesListViewModel(private val messagesRepo: MessagesRepository) : ViewModel() {
    val totals: LiveData<MessagesTotals> =
        messagesRepo.messagesTotals

    private val queryParams = MutableLiveData<Pair<Int, ProcessingState?>>(CHUNK_SIZE to null)
    val filter: LiveData<ProcessingState?> = queryParams.map { it.second }

    private val _messages = MediatorLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    init {
        _messages.addSource(
            queryParams.switchMap { (limit, state) ->
                messagesRepo.selectLast(limit, state)
            }
        ) {
            _messages.value = it
            hasMore = it.size >= (queryParams.value?.first ?: CHUNK_SIZE)
            isLoading = false
        }
        loadMore()
    }

    private var isLoading = false
    private var hasMore = true

    fun loadMore(index: Int = 0) {
        val currentLimit = queryParams.value?.first ?: 0
        if (currentLimit >= index + CHUNK_SIZE || isLoading || !hasMore) return

        isLoading = true
        queryParams.value = (currentLimit + CHUNK_SIZE) to queryParams.value?.second
    }

    fun setFilter(state: ProcessingState?) {
        val current = queryParams.value
        if (current?.second == state) return

        queryParams.value = CHUNK_SIZE to state
        hasMore = true
    }

    companion object {
        private const val CHUNK_SIZE = 50
    }
}
