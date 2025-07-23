package com.yusufteker.worthy.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yusufteker.worthy.core.data.database.db.DatabaseFactory
import com.yusufteker.worthy.core.data.database.db.WorthyDatabase
import com.yusufteker.worthy.core.data.database.repository.CategoryRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.ExpenseRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.IncomeRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.RecurringFinancialItemRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.WishlistRepositoryImpl
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import com.yusufteker.worthy.core.domain.repository.RecurringFinancialItemRepository
import com.yusufteker.worthy.core.domain.repository.WishlistRepository
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.screen.dashboard.presentation.DashboardViewModel
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.onboarding.presentation.OnboardingViewModel
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.settings.presentation.SettingsViewModel
import com.yusufteker.worthy.screen.wishlist.presentation.WishlistViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.yusufteker.routealarm.core.presentation.popup.PopupManager

expect val platformModule: Module

val sharedModule = module {

    single { PopupManager() }
    single { UserPrefsManager(get()) }
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
    single { get<WorthyDatabase>().wishlistDao }
    single { get<WorthyDatabase>().categoryDao }
    single { get<WorthyDatabase>().recurringFinancialItemDao }

    // Repository implementasyonlarını bind et
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
    single<IncomeRepository> { IncomeRepositoryImpl(get()) }
    single<WishlistRepository> { WishlistRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<RecurringFinancialItemRepository> { RecurringFinancialItemRepositoryImpl(get()) }



    viewModel { OnboardingViewModel(get()) }
    viewModel { BaseViewModel() }
    viewModel { DashboardViewModel(get()) }
    viewModel { SettingsViewModel(get(),get(),get(),get(), get() )}
    viewModel { WishlistViewModel() }
}