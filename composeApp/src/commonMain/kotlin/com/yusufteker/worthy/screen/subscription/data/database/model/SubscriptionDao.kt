package com.yusufteker.worthy.screen.subscription.data.database.model


import androidx.room.*
import com.yusufteker.worthy.screen.subscription.data.database.entities.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscription: SubscriptionEntity)

    @Update
    suspend fun update(subscription: SubscriptionEntity)

    @Query("DELETE FROM subscriptions WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM subscriptions WHERE groupId = :groupId")
    suspend fun deleteByGroupId(groupId: String)

    @Delete
    suspend fun deleteAll(subscriptions: List<SubscriptionEntity>)

    @Upsert
    suspend fun upsertAll(subscriptions: List<SubscriptionEntity>)

    @Query("SELECT * FROM subscriptions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): SubscriptionEntity?

    @Query("SELECT * FROM subscriptions ORDER BY name ASC")
    fun getAll(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE groupId = :groupId ORDER BY startDate DESC")
     fun getAllByGroupId(groupId: String): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE cardId = :cardId ORDER BY name ASC")
    fun getByCardId(cardId: Int): Flow<List<SubscriptionEntity>>


    @Query(
        """
    SELECT * FROM subscriptions
    WHERE (endDate IS NULL OR endDate >= :today)
        AND startDate <= :today
    ORDER BY startDate
    """
    )
    fun getActiveSubscriptions(today: Int): Flow<List<SubscriptionEntity>>


    @Query("""
SELECT * FROM subscriptions
WHERE 
    (startDate >= :startInt)
    OR 
    (startDate < :startInt AND (endDate IS NULL OR endDate >= :startInt))
ORDER BY startDate
""")
    fun getSubscriptionsSince(startInt: Int): Flow<List<SubscriptionEntity>>

}
