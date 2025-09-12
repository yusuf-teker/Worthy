package com.yusufteker.worthy.screen.installments.list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.isBeforeToday
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.util.formatted
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel

//TODO tasarım düzenlenecek
@Composable
fun InstallmentCard(installment: InstallmentCardUIModel) {
    val transaction = installment.transaction
    val isPast = transaction.transactionDate.toAppDate().isBeforeToday()
    val textColor = if (isPast) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val amountColor = if (isPast) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )
                Text(
                    text = transaction.amount.formatted(),
                    style = MaterialTheme.typography.titleMedium,
                    color = amountColor
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Kesim: ${installment.card?.statementDay}/${transaction.transactionDate.toAppDate().month}",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor
                )

                Text(
                    text = "${(transaction.installmentIndex ?: 0) + 1}/${transaction.installmentCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor
                )
            }
        }
    }
}
