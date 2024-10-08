package app.dotoo.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [LogicModule::class, ClientModule::class])
@ComponentScan("app.dotoo.data")
class DataModule
