#  Fragment persistence

## How to retain an instance of a fragment on configuration change (ex: device rotation)
public void setRetainInstance (boolean retain)
Control whether a fragment instance is retained across Activity re-creation (such as from a configuration change). This can only be used with fragments not in the back stack. If set, the fragment lifecycle will be slightly different when an activity is recreated:

Source: https://stackoverflow.com/questions/11160412/why-use-fragmentsetretaininstanceboolean