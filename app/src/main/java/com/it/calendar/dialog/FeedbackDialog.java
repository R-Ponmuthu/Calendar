package com.it.calendar.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.it.calendar.R;
import com.it.calendar.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class FeedbackDialog extends AppCompatDialog implements View.OnClickListener {

    private static final String SESSION_COUNT = "session_count";
    private static final String SHOW_NEVER = "show_never";
    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String FEEDBACK_KEY = "Feedback";
    private String MyPrefs = "RatingDialog";
    private SharedPreferences sharedpreferences;
    private Context context;
    private Builder builder;
    private TextView tvFeedback, tvSubmit, tvCancel, tvFeedbackHint;
    private ImageView ivIcon;
    private EditText etFeedback, etName, etEmail;
    private LinearLayout ratingButtons, feedbackButtons;
    private float threshold;
    private int session;
    private boolean thresholdPassed = true;
    private FirebaseFirestore firebaseFirestore;

    public FeedbackDialog(Context context, Builder builder) {
        super(context);
        this.context = context;
        this.builder = builder;

        this.session = builder.session;
        this.threshold = builder.threshold;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.feedback_layout);

        tvFeedback = findViewById(R.id.dialog_rating_feedback_title);
        tvFeedbackHint = findViewById(R.id.dialog_rating_feedback_hint);
        tvSubmit = findViewById(R.id.dialog_rating_button_feedback_submit);
        tvCancel = findViewById(R.id.dialog_rating_button_feedback_cancel);
        ivIcon = findViewById(R.id.dialog_rating_icon);
        etFeedback = findViewById(R.id.dialog_rating_feedback);
        etName = findViewById(R.id.username);
        etEmail = findViewById(R.id.emailId);
        ratingButtons = findViewById(R.id.dialog_rating_buttons);
        feedbackButtons = findViewById(R.id.dialog_rating_feedback_buttons);

        firebaseFirestore = FirebaseFirestore.getInstance();

        init();
    }

    private void init() {

        tvFeedback.setText(builder.formTitle);
        tvSubmit.setText(builder.submitText);
        tvCancel.setText(builder.cancelText);
        tvFeedbackHint.setHint(builder.feedbackFormHint);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);

        tvFeedback.setTextColor(builder.titleTextColor != 0 ? ContextCompat.getColor(context, builder.titleTextColor) : ContextCompat.getColor(context, R.color.black));

        if (builder.feedBackTextColor != 0) {
            etFeedback.setTextColor(ContextCompat.getColor(context, builder.feedBackTextColor));
            etName.setTextColor(ContextCompat.getColor(context, builder.feedBackTextColor));
            etEmail.setTextColor(ContextCompat.getColor(context, builder.feedBackTextColor));
        }

        Drawable d = context.getPackageManager().getApplicationIcon(context.getApplicationInfo());
        ivIcon.setImageDrawable(builder.drawable != null ? builder.drawable : d);

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.dialog_rating_button_feedback_submit) {

            String feedback = etFeedback.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (!TextUtils.isEmpty(feedback) && feedback.length() >= 5 && !TextUtils.isEmpty(email)) {

                if (new Utils().isOnline(context))
                    submitFeedback(etEmail.getText().toString().trim(), etName.getText().toString().trim(), etFeedback.getText().toString().trim());
                else
                    Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();

            } else {

                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                etFeedback.startAnimation(shake);
                etEmail.startAnimation(shake);
            }

        } else if (view.getId() == R.id.dialog_rating_button_feedback_cancel) {

            dismiss();
        }

    }

    private void submitFeedback(String email, String name, String feedBack) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending feedback...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> newFeedback = new HashMap<>();
        newFeedback.put(NAME_KEY, name);
        newFeedback.put(EMAIL_KEY, email);
        newFeedback.put(FEEDBACK_KEY, feedBack);

        firebaseFirestore.collection("Calendar").document("Feedback").set(newFeedback)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        dismiss();
                        Toast.makeText(context, "Feedback Sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "ERROR" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });

    }

    @Override
    public void show() {

        if (checkIfSessionMatches(session)) {
            super.show();
        }
    }

    private boolean checkIfSessionMatches(int session) {

        if (session == 1) {
            return true;
        }

        sharedpreferences = context.getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);

        if (sharedpreferences.getBoolean(SHOW_NEVER, false)) {
            return false;
        }

        int count = sharedpreferences.getInt(SESSION_COUNT, 1);

        if (session == count) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(SESSION_COUNT, 1);
            editor.commit();
            return true;
        } else if (session > count) {
            count++;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(SESSION_COUNT, count);
            editor.commit();
            return false;
        } else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(SESSION_COUNT, 2);
            editor.commit();
            return false;
        }
    }

    private void showNever() {
        sharedpreferences = context.getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(SHOW_NEVER, true);
        editor.commit();
    }

    public static class Builder {

        private final Context context;
        private String playstoreUrl;
        private String formTitle, submitText, cancelText, feedbackFormHint;
        private int titleTextColor;
        private int feedBackTextColor;
        private Drawable drawable;

        private int session = 1;
        private float threshold = 1;

        public Builder(Context context) {
            this.context = context;
            // Set default PlayStore URL
            this.playstoreUrl = "market://details?id=" + context.getPackageName();
            initText();
        }

        private void initText() {

            formTitle = context.getString(R.string.rating_dialog_feedback_title);
            submitText = context.getString(R.string.rating_dialog_submit);
            cancelText = context.getString(R.string.rating_dialog_cancel);
            feedbackFormHint = context.getString(R.string.rating_dialog_suggestions);
        }

        public Builder session(int session) {
            this.session = session;
            return this;
        }

        public Builder threshold(float threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder icon(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }


        public Builder titleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        /*public Builder icon(int icon) {
            this.icon = icon;
            return this;
        }*/

        public Builder formTitle(String formTitle) {
            this.formTitle = formTitle;
            return this;
        }

        public Builder formHint(String formHint) {
            this.feedbackFormHint = formHint;
            return this;
        }

        public Builder formSubmitText(String submitText) {
            this.submitText = submitText;
            return this;
        }

        public Builder formCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Builder feedbackTextColor(int feedBackTextColor) {
            this.feedBackTextColor = feedBackTextColor;
            return this;
        }

        public Builder playstoreUrl(String playstoreUrl) {
            this.playstoreUrl = playstoreUrl;
            return this;
        }

        public FeedbackDialog build() {
            return new FeedbackDialog(context, this);
        }

    }
}
