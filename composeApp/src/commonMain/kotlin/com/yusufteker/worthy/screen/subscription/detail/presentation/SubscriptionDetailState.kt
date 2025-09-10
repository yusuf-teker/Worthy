package com.yusufteker.worthy.screen.subscription.detail.presentation
        
        import androidx.compose.foundation.Canvas
        import androidx.compose.foundation.layout.fillMaxWidth
        import androidx.compose.foundation.layout.height
        import androidx.compose.foundation.layout.padding
        import androidx.compose.material3.Card
        import androidx.compose.material3.CardDefaults
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.graphics.Path
        import androidx.compose.ui.graphics.StrokeCap
        import androidx.compose.ui.graphics.drawscope.Stroke
        import androidx.compose.ui.unit.Dp
        import androidx.compose.ui.unit.dp
        import com.yusufteker.worthy.core.domain.getCurrentAppDate
        import com.yusufteker.worthy.core.domain.model.AppDate
        import com.yusufteker.worthy.core.domain.model.Money
        import com.yusufteker.worthy.core.domain.model.RecurringItem
        import com.yusufteker.worthy.core.domain.model.toEpochMillis
        import com.yusufteker.worthy.core.presentation.base.BaseState
        import com.yusufteker.worthy.core.presentation.theme.AppColors
        import com.yusufteker.worthy.screen.card.domain.model.Card

data class SubscriptionDetailState(
            override val isLoading: Boolean = false,
            val errorMessage: String? = null,
            val subscription: RecurringItem.Subscription? = null,
            val subscriptions: List<RecurringItem.Subscription> = emptyList(),
            val card: Card? = null,
            val activeStreak: Int? = null,

            val pickedDate: AppDate = getCurrentAppDate(day = 1)
        ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
            return this.copy(isLoading = isLoading)

        }
}
