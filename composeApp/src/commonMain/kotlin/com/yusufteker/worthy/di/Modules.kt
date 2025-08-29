package com.yusufteker.worthy.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yusufteker.worthy.core.data.database.db.DatabaseFactory
import com.yusufteker.worthy.core.data.database.db.WorthyDatabase
import com.yusufteker.worthy.core.data.database.repository.CategoryRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.DashboardRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.OnboardingRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.RecurringFinancialItemRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.SearchHistoryRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.TransactionRepositoryImpl
import com.yusufteker.worthy.core.data.service.DefaultCurrencyConverter
import com.yusufteker.worthy.core.data.service.datasource.CurrencyRatesCacheDataSourceImpl
import com.yusufteker.worthy.core.data.service.datasource.CurrencyRatesRemoteDataSourceImpl
import com.yusufteker.worthy.core.data.service.repository.CurrencyRatesRepositoryImpl
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.domain.repository.SearchHistoryRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesCacheDataSource
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesRemoteDataSource
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import com.yusufteker.worthy.screen.addtransaction.presentation.AddTransactionViewModel
import com.yusufteker.worthy.screen.analytics.data.repository.AnalyticsRepositoryImpl
import com.yusufteker.worthy.screen.analytics.domain.repository.AnalyticsRepository
import com.yusufteker.worthy.screen.analytics.presentation.AnalyticsViewModel
import com.yusufteker.worthy.screen.card.data.database.repository.CardRepositoryImpl
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.card.add.presentation.AddCardViewModel
import com.yusufteker.worthy.screen.card.list.presentation.CardListViewModel
import com.yusufteker.worthy.screen.dashboard.domain.DashboardRepository
import com.yusufteker.worthy.screen.dashboard.presentation.DashboardViewModel
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingRepository
import com.yusufteker.worthy.screen.onboarding.presentation.OnboardingViewModel
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.settings.presentation.SettingsViewModel
import com.yusufteker.worthy.screen.subscription.add.presentation.AddSubscriptionViewModel
import com.yusufteker.worthy.screen.subscription.data.database.repository.SubscriptionRepositoryImpl
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import com.yusufteker.worthy.screen.subscription.list.presentation.SubscriptionListViewModel
import com.yusufteker.worthy.screen.subscription.detail.presentation.SubscriptionDetailViewModel
import com.yusufteker.worthy.screen.wishlist.add.presentation.WishlistAddViewModel
import com.yusufteker.worthy.screen.wishlist.detail.presentation.WishlistDetailViewModel
import com.yusufteker.worthy.screen.wishlist.list.data.database.repository.WishlistRepositoryImpl
import com.yusufteker.worthy.screen.wishlist.list.presentation.WishlistViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.yusufteker.routealarm.core.presentation.popup.PopupManager

expect val platformModule: Module

val sharedModule = module {

    single { PopupManager() }

    // DATA STORE
    single { UserPrefsManager(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    single { OnboardingManager(get()) }



    // Veritabanı oluşturma
    single { // Create Database
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    // DAO'ları veritabanından çek
    single { get<WorthyDatabase>().transactionDao }
    single { get<WorthyDatabase>().wishlistItemDao }
    single { get<WorthyDatabase>().categoryDao }
    single { get<WorthyDatabase>().recurringFinancialItemDao }
    single { get<WorthyDatabase>().cardDao }
    single { get<WorthyDatabase>().subscriptionDao }

    // Repository implementasyonlarını bind et
    single<CurrencyRatesRepository> { CurrencyRatesRepositoryImpl(get(), get()) }
    single<CurrencyConverter> { DefaultCurrencyConverter(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), get(), get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(get(),get(),get()) }
    single<WishlistRepository> { WishlistRepositoryImpl(get(),get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<RecurringFinancialItemRepository> { RecurringFinancialItemRepositoryImpl(get()) }
    single<DashboardRepository> { DashboardRepositoryImpl(get(), get(),get(),get()) }
    single<CurrencyRatesCacheDataSource> { CurrencyRatesCacheDataSourceImpl() }
    single<CurrencyRatesRemoteDataSource> { CurrencyRatesRemoteDataSourceImpl() }
    single<CardRepository> { CardRepositoryImpl(get()) }
    single<AnalyticsRepository> { AnalyticsRepositoryImpl(get(), get(), get(), get()) }
    single<SubscriptionRepository> { SubscriptionRepositoryImpl(get(), get(), get()) }

    viewModel { OnboardingViewModel(get()) }
    viewModel { DashboardViewModel(get(),get(), get()) }
    viewModel { SettingsViewModel(get(),get(),get(),get() )}
    viewModel { WishlistViewModel(get(), get()) }
    viewModel { WishlistAddViewModel(get(),get(),get()) }
    viewModel { AddTransactionViewModel(get()) }
    viewModel { AddCardViewModel(get()) }
    viewModel { CardListViewModel(get()) }
    viewModel { WishlistDetailViewModel() }
    viewModel { AnalyticsViewModel(get(), get()) }
    viewModel { SubscriptionListViewModel(get()) }
    viewModel { AddSubscriptionViewModel(get(), get()) }
    viewModel { SubscriptionDetailViewModel(get()) }
}
