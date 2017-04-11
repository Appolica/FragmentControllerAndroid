# FragmentControllerAndroid

This library wrapps around the Android's FragmentManager and provides an easy to use API. You can add fragments without bothering about the FragmentTransaction. Check the API to see what it provides.

FragmentControllerAndroid is developed and maintained by [Appolica](http://www.appolica.com/).

## Download

## Gradle

## Example

### Create the controller

Add a container for the FragmentController fragment in your layout:

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</FrameLayout>
```
Instantiate the controller by passing it a root FragmentProvider:
```java
final FragmentController frController = FragmentController.instance(rootProvider);
```

Then add the controller fragment:
```java
getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, frController, CONTROLLER_TAG)
                    .commit();
```

And basically you're ready to go. From now on you won't bother for any transactions. All your fragments will be added and managed within the controller fragment using its child fragment manager. 

### Use the controller
Once the controller is added to the FragmentManager, it will show the root fragment. You can add another one the following way:

```java
frController.pushBody()
                .addToBackStack(true)
                .withAnimation(true)
                .fragment(secondFragmentProvider)
                .push();
```

Want to add custom animation? No problem:
```java
frController.pushBody()
                .addToBackStack(true)
                .customAnimation()
                .enter(R.anim.slide_in_right)
                .exit(R.anim.slide_out_left)
                .popEnter(android.R.anim.slide_in_left)
                .popExit(android.R.anim.slide_out_right)
                .end()
                .fragment(secondFragmentProvider)
                .push();
```

If you are inside one of the controller's children and you want to obtain the controller's instance, you can do so by using
this handy method:
```java
final FragmentController frController = FragmentUtil.getFragmentController(this);
```

Popping a fragment is also as easy as possible:
```java
FragmentUtil.getFragmentController(this).pop(true);
```

Go to [Wiki](https://github.com/Appolica/FragmentControllerAndroid/wiki) for more information.
