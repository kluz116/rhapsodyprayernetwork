package org.allan_musembya.prayer.prayernetwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

public class Purchase extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_app_bar);

        CardInputWidget mCardInputWidget = findViewById(R.id.card_input_widget);

        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            //mErrorDialogHandler.showError("Invalid Card Data");
            Toast.makeText(this, "Invalid Card Data", Toast.LENGTH_SHORT).show();
        }
    }
}
