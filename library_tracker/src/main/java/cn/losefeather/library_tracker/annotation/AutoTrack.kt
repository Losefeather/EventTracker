package cn.losefeather.library_tracker.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AutoTrack(val pageName: String = "")
