package cn.losefeather.library_tracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.losefeather.library_tracker.database.DataBaseContact.TABLE.TABLE_USER_INFO
import cn.losefeather.library_tracker.entity.UserInfo


@Dao
interface UserDao {
    @Query("SELECT * FROM $TABLE_USER_INFO")
    fun getAll(): List<UserInfo>

    @Query("SELECT * FROM $TABLE_USER_INFO WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserInfo>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): UserInfo

    @Insert
    fun insertAll(vararg users: UserInfo)

    @Delete
    fun delete(user: UserInfo)
}