package com.yusufteker.worthy.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yusufteker.worthy.core.data.database.db.DatabaseFactory
import com.yusufteker.worthy.core.data.database.db.WorthyDatabase
import com.yusufteker.worthy.core.data.database.repository.CategoryRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.ExpenseRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.IncomeRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.RecurringFinancialItemRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.SearchHistoryRepositoryImpl
import com.yusufteker.worthy.core.data.service.DefaultCurrencyConverter
import com.yusufteker.worthy.core.data.service.datasource.CurrencyRatesCacheDataSourceImpl
import com.yusufteker.worthy.core.data.service.datasource.CurrencyRatesRemoteDataSourceImpl
import com.yusufteker.worthy.core.data.service.repository.CurrencyRatesRepositoryImpl
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.CurrencyRatesRepository
import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.domain.repository.SearchHistoryRepository
import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesCacheDataSource
import com.yusufteker.worthy.core.domain.service.datasource.CurrencyRatesRemoteDataSource
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.screen.dashboard.presentation.DashboardViewModel
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.onboarding.presentation.OnboardingViewModel
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.settings.presentation.SettingsViewModel
import com.yusufteker.worthy.screen.wishlist.add.presentation.WishlistAddViewModel
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
    single { get<WorthyDatabase>().expenseDao }
    single { get<WorthyDatabase>().incomeDao }
    single { get<WorthyDatabase>().wishlistItemDao }
    single { get<WorthyDatabase>().categoryDao }
    single { get<WorthyDatabase>().recurringFinancialItemDao }

    // Repository implementasyonlarını bind et
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
    single<IncomeRepository> { IncomeRepositoryImpl(get()) }
    single<WishlistRepository> { WishlistRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<RecurringFinancialItemRepository> { RecurringFinancialItemRepositoryImpl(get()) }

    single<CurrencyRatesCacheDataSource> { CurrencyRatesCacheDataSourceImpl() }
    single<CurrencyRatesRemoteDataSource> { CurrencyRatesRemoteDataSourceImpl() }

    single<CurrencyRatesRepository> { CurrencyRatesRepositoryImpl(get(), get()) }
    single<CurrencyConverter> { DefaultCurrencyConverter(get()) }


    viewModel { OnboardingViewModel(get()) }
    viewModel { BaseViewModel() }
    viewModel { DashboardViewModel(get()) }
    viewModel { SettingsViewModel(get(),get(),get(),get(), get(), get() )}
    viewModel { WishlistViewModel(get(), get(), get(), get()) }
    viewModel { WishlistAddViewModel(get(),get(),get()) }
}