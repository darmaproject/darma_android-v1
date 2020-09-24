package com.darma.wallet.base




interface IPresenter<in V: IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()

}
