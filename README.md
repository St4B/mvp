# mvp ![Build Status](https://travis-ci.com/St4B/mvp.svg?branch=master)
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

It is not consistent to execute actions in presenter's constructor. Instead use onPresenterCreated() method of your ui component to perform action on Presenter's initiation.

Also the presenter tries to execute pending ui actions on app's restore (In the scenario that was killed by the OS). You can enhance this behaviour by overriding onRestore() method of your Presenter.

If you want a field to be persisted even if your app was killed by the OS, you must use ``` @Persistent``` annotation. For example:

```java
public class TimerPresenter extends Presenter<TimerElement> {

    @Persistent
    private int mSeconds = 0;
    
}
```

Download
--------

```groovy
dependencies {
  implementation 'com.quadible:mvp:1.0'
  annotationProcessor 'com.quadible:mvp-processor:1.1'

  //You also need to include appcompat and Gson
  implementation 'com.android.support:appcompat-v7:+'
  implementation 'com.google.code.gson:gson:2.+'
}
```

AndroidX Migration
--------

```groovy
dependencies {
  implementation 'com.quadible:mvp:2.0.0'
  annotationProcessor 'com.quadible:mvp-processor:2.0.0'

  //You also need to include appcompat and Gson
  implementation 'androidx.appcompat:appcompat:1.0.0'
  implementation 'com.google.code.gson:gson:2.+'
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
