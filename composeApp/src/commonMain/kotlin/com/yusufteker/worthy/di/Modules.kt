package com.yusufteker.worthy.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yusufteker.worthy.core.data.database.db.DatabaseFactory
import com.yusufteker.worthy.core.data.database.db.WorthyDatabase
import com.yusufteker.worthy.core.data.database.repository.CategoryRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.ExpenseRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.IncomeRepositoryImpl
import com.yusufteker.worthy.core.data.database.repository.WishlistRepositoryImpl
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import com.yusufteker.worthy.core.domain.repository.WishlistRepository
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.feature.dashboard.presentation.DashboardViewModel
import com.yusufteker.worthy.feature.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.feature.onboarding.presentation.OnboardingViewModel
import com.yusufteker.worthy.feature.settings.domain.UserPrefsManager
import com.yusufteker.worthy.feature.settings.presentation.SettingsViewModel
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

    // Repository implementasyonlarını bind et
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
    single<IncomeRepository> { IncomeRepositoryImpl(get()) }
    single<WishlistRepository> { WishlistRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }



    viewModel { OnboardingViewModel(get()) }
    viewModel { BaseViewModel() }
    viewModel { DashboardViewModel(get()) }
    viewModel { SettingsViewModel(get(),get(),get(),get() )}
}