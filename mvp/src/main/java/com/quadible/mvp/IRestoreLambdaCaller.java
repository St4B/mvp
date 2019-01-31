package com.quadible.mvp;

import com.quadible.mvp.Presenter.UiAction;

/**
 * <p>
 *     Because of lambda expression is not serialized (we just store the name of lambda expression's
 *     class), we need to provide a mechanism for binding the created lambda expression with its
 *     caller.
 * </p>
 */
interface IRestoreLambdaCaller {

    void restore(Presenter presenter, UiAction action);

}
