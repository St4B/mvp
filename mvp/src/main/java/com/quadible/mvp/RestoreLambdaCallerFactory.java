package com.quadible.mvp;

import com.quadible.mvp.Presenter.UiAction;
import java.lang.reflect.Field;

/**
 * <p>
 *     Factory for getting the correct {@link IRestoreLambdaCaller} based on type of lambda
 *     expression Namely, if the lambda expression is
 * </p>
 */
class RestoreLambdaCallerFactory {

    private final PresenterMethodLambdaCaller mPresenterMethodLambdaCaller =
            new PresenterMethodLambdaCaller();

    private final InnerClassLambdaCaller mInnerClassLambdaCaller = new InnerClassLambdaCaller();

    private final ImplementedInterfaceLambdaCaller mImplementedInterfaceLambdaCaller =
            new ImplementedInterfaceLambdaCaller();

    private final NullRestoreLambdaCaller mNullRestoreLambdaCaller = new NullRestoreLambdaCaller();

    IRestoreLambdaCaller create(UiAction action) {
        Field[] fields = action.getClass().getDeclaredFields();
        for (Field classField : fields) {
            if ("arg$1".equals(classField.getName())) {
                if (classField.getClass().isAssignableFrom(Presenter.class)) {
                    return mPresenterMethodLambdaCaller;
                } else {
                    return mInnerClassLambdaCaller;
                }

            } else if ( "this$0".equals(classField.getName())) {
                return mImplementedInterfaceLambdaCaller;
            }

        }
        return mNullRestoreLambdaCaller;
    }

}
