package com.quadible.mvp;

import com.quadible.mvp.Presenter.UiAction;

import java.lang.reflect.Field;

/**
 * <p>
 *
 * </p>
 */
class PresenterMethodLambdaCaller implements IRestoreLambdaCaller {

    @Override
    public void restore(Presenter presenter, UiAction action) {
        Field[] fields = action.getClass().getDeclaredFields();
        for (Field classField : fields) {
            if ("arg$1".equals(classField.getName())) {
                try {
                    boolean accessible = classField.isAccessible();
                    classField.setAccessible(true);
                    classField.set(action, presenter);
                    classField.setAccessible(accessible);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
