package com.darma.wallet.widget.gestureunlock;

public class JsConst {
    public static final String CALLBACK_IS_GESTURE_CODE_SET = "uexGestureUnlock.cbIsGestureCodeSet";
    public static final String CALLBACK_VERIFY = "uexGestureUnlock.cbVerify";
    public static final String CALLBACK_CREATE = "uexGestureUnlock.cbCreate";
    public static final String ON_EVENT_OCCUR = "uexGestureUnlock.onEventOccur";

    public static final int POINT_STATE_NORMAL = 0;

    public static final int POINT_STATE_SELECTED = 1;

    public static final int POINT_STATE_WRONG = 2;

    public static final int ERROR_CODE_NONE_GESTURE = 1;
    public static final int ERROR_CODE_CANCEL_CREATE = 2;
    public static final int ERROR_CODE_CANCEL_VERIFY = 3;
    public static final int ERROR_CODE_TOO_MANY_TRY = 4;
    public static final int ERROR_CODE_CANCEL_OUTSIDE = 5;
    public static final int ERROR_CODE_UNKNOWN = 6;

    public static final int EVENT_PLUGIN_INIT = 1;// plugin_uexGestureUnlock_plugin_init
    public static final int EVENT_START_VERIFY = 2;// plugin_uexGestureUnlock_start_verify
    public static final int EVENT_VERIFY_ERROR = 3;// plugin_uexGestureUnlock_verify_error
    public static final int EVENT_CANCEL_VERIFY = 4;// plugin_uexGestureUnlock_cancel_verify
    public static final int EVENT_VERIFY_SUCCESS = 5;// plugin_uexGestureUnlock_verify_success
    public static final int EVENT_START_CREATE = 6;// plugin_uexGestureUnlock_start_create
    public static final int EVENT_LENGTH_ERROR = 7;// plugin_uexGestureUnlock_length_error
    public static final int EVENT_SECOND_INPUT = 8;// plugin_uexGestureUnlock_second_input
    public static final int EVENT_NOT_SAME = 9;// plugin_uexGestureUnlock_not_same
    public static final int EVENT_CANCEL_CREATE = 10;// plugin_uexGestureUnlock_cancel_create
    public static final int EVENT_CREATE_SUCCESS = 11;// plugin_uexGestureUnlock_create_success

}
