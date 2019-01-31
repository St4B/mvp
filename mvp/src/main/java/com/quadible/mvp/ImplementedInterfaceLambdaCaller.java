package com.quadible.mvp;

import com.quadible.mvp.Presenter.UiAction;

import java.lang.reflect.Field;

/**
 * <p>
 *      There is the possibility of implementing our functional interface ({@link UiAction}) and just
 *      pass the instance of the implementation in the places where we expect a lambda expression.
 *      This class is responsible attaching
 * </p>
 */
class ImplementedInterfaceLambdaCaller implements IRestoreLambdaCaller {

    @Override
    public void restore(Presenter presenter, UiAction action) {
        Field[] fields = action.getClass().getDeclaredFields();
        for (Field classField : fields) {
            if ("this$0".equals(classField.getName())) {
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
