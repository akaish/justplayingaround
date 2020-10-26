package com.example.yaplayground.mvp

class MVPContract {

    interface Presenter<in T> {
        fun subscribe()
        fun unsubscribe()
        fun attach(view: T): Presenter<T>
    }

    interface View
}