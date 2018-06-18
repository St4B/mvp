# mvp
Android mvp simple implementation.

 * Avoid checking if presenter is attached or not.
 * Persist presenter over configuration changes.

At first you have to install mvp in your application
```java
public class App extends Application { 

    @Override
    public void onCreate() {
        super.onCreate();
        Mvp.install(this);
    }
    
}
```

Then you have to declare Presenter and UiElement (which is the contract between presenter and your ui component)
```java
public interface MainElement extends UiElement<MainPresenter> {

    void updateText(String text);

}

public class MainPresenter extends Presenter<MainElement> {

    public void getText() {
        post(new TextUpdater());
    }

    private class TextUpdater extends UiAction<MainElement> {

        @Override
        public void act() { 
            getUi().updateText("updated text");
        }
    }

}
```
And last but not least declare your ui component which will use your presenter. Extend something from:
  * BaseMvpActivity
  * BaseMvpAppCompatDialogFragment
  * BaseMvpDialogFragment
  * BaseMvpFragment
  * BaseMvpFragmentActivity
```java
public class MainActivity
        extends BaseMvpActivity<MainElement, MainPresenter> implements MainElement {

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }
    
    @Override
    public void updateText(String text) {
        //do something with the text
    }

}
```

It is not consistent to execute actions in presenter's contructor. Instead use onPresenterCreated() method of your ui component to perform action on Presenter's initiation.

Also the presenter tries to execute pending ui actions on app's restore (In the scenario that was killed by the OS). You can enhance this behaviour by overriding onRestore() method of your Presenter.

If you want a field to not be serialized you must define it as transient. For example you do not need to keep objects which contain business logic. These object could recreated in onRestore method. Presenter should only serialize data (ids,models,states etc)

Download
--------

```groovy
dependencies {
  implementation 'com.quadible:mvp:0.21-beta'
}
```

## License
```
 Copyright 2017 Quadible Ltd.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```
