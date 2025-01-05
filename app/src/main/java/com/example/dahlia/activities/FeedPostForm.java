package com.example.dahlia.activities;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.dahlia.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FeedPostForm extends DialogFragment {


    public interface FeedPostFormListener {
        void onFeedPostSubmit(String title, String description);
    }

    private FeedPostFormListener listener;


    public void setListener(FeedPostFormListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_feed_post_form, null);


        EditText editTextTitle = view.findViewById(R.id.editTextTitle);
        EditText editTextDescription = view.findViewById(R.id.editTextDescription);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);


        buttonSubmit.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();


            if (!title.isEmpty() && !description.isEmpty()) {

                if (listener != null) {
                    listener.onFeedPostSubmit(title, description);
                }


                dismiss();
            } else {
                showToast("Please fill in all fields");
            }
        });

        Typeface customTypeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_bold);

        SpannableString spannableTitle = new SpannableString("Post Something");
        spannableTitle.setSpan(
                new CustomTypefaceSpan("", customTypeface),
                0,
                spannableTitle.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .setTitle(spannableTitle)
                .create();
    }

    private static class CustomTypefaceSpan extends TypefaceSpan {

        private final Typeface newTypeface;

        public CustomTypefaceSpan(String family, Typeface typeface) {
            super(family);
            this.newTypeface = typeface;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeface(ds, newTypeface);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeface(paint, newTypeface);
        }

        private void applyCustomTypeface(TextPaint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


}