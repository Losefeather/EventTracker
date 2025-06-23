package cn.losefeather.library_tracker.database.dao


//@Dao
//interface UserDao {
//    @Query("SELECT * FROM $TABLE_USER_INFO")
//    suspend fun getAll(): List<UserInfo>
//
//    @Query("SELECT * FROM $TABLE_USER_INFO WHERE uid IN (:userIds)")
//    suspend fun loadAllByIds(userIds: IntArray): List<UserInfo>
//
////    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
////            "last_name LIKE :last LIMIT 1")
////    fun findByName(first: String, last: String): UserInfo
//
//    @Insert
//    suspend fun insertAll(vararg users: UserInfo)
//
//    @Delete
//    suspend fun delete(vararg user: UserInfo)
//}