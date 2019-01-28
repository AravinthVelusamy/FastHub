package com.fastaccess.github.di.modules

import com.fastaccess.github.di.scopes.PerActivity
import com.fastaccess.github.ui.modules.auth.LoginChooserActivity
import com.fastaccess.github.ui.modules.issue.IssueActivity
import com.fastaccess.github.ui.modules.main.MainActivity
import com.fastaccess.github.ui.modules.multipurpose.MultiPurposeActivity
import com.fastaccess.github.ui.modules.profile.ProfileActivity
import com.fastaccess.github.ui.modules.trending.TrendingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Kosh on 12.05.18.
 */
@Module()
abstract class ActivityBindingModule {
    @PerActivity @ContributesAndroidInjector(modules = [FragmentBindingModule::class]) abstract fun mainActivity(): MainActivity
    @PerActivity @ContributesAndroidInjector(modules = [FragmentBindingModule::class]) abstract fun loginChooser(): LoginChooserActivity
    @PerActivity @ContributesAndroidInjector(modules = [FragmentBindingModule::class]) abstract fun profileActivity(): ProfileActivity
    @PerActivity @ContributesAndroidInjector(modules = [FragmentBindingModule::class]) abstract fun multiPurposeActivity(): MultiPurposeActivity
    @PerActivity @ContributesAndroidInjector(modules = [FragmentBindingModule::class]) abstract fun trendingActivity(): TrendingActivity
    @PerActivity @ContributesAndroidInjector(modules = [FragmentBindingModule::class]) abstract fun trendingIssueActivity(): IssueActivity
}