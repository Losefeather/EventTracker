package cn.losefeather.library_tracker

import cn.losefeather.library_tracker.entity.EventInfo

class EventCache {
    private val eventCache = ArrayList<EventInfo>()

    fun addEvent(event: EventInfo) {
        eventCache.add(event)
    }


    fun clearAllEvent() {
        eventCache.clear()
    }


    fun getAllCacheEvent(): ArrayList<EventInfo> {
        return eventCache
    }

    fun removeEvent(event: EventInfo) {
        eventCache.remove(event)
    }

    fun removeEvents(events: List<EventInfo>) {
        eventCache.removeAll(events)
    }
}
