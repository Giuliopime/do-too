package app.dotoo.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("app.dotoo.core.logic")
class LogicModule
