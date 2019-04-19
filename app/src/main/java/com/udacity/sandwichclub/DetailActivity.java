package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private TextView mAlsoKnownTv;
    private TextView mAlsoKnownLabel;
    private TextView mOriginTv;
   private TextView mOriginLabel;
    private TextView mDescriptionTv;
    private TextView mIngredientTv;
    private ImageView mSandwichIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mSandwichIv = findViewById(R.id.image_iv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mSandwichIv);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        mOriginTv = findViewById(R.id.origin_tv);
        mAlsoKnownLabel = findViewById(R.id.alsoKnownAs_label);
        mAlsoKnownTv = findViewById(R.id.also_known_tv);
        mOriginLabel = findViewById(R.id.placeOfOrigin_label);
        mDescriptionTv = findViewById(R.id.description_tv);
        mIngredientTv = findViewById(R.id.ingredients_tv);

        String originText = sandwich.getPlaceOfOrigin();

        if (originText.isEmpty()){
            mOriginTv.setText(R.string.detail_error_message);

        } else {
            mOriginTv.setText(originText);
        }

        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getAlsoKnownAs().get(0));

            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append(sandwich.getAlsoKnownAs().get(i));
            }
            mAlsoKnownTv.setText(stringBuilder.toString());
        } else {
            mAlsoKnownTv.setVisibility(View.GONE);
            mAlsoKnownLabel.setVisibility(View.GONE);
        }

        List<String> ingredientsList = sandwich.getIngredients();
        if (ingredientsList.size() == 0){
            mIngredientTv.setText(R.string.detail_error_message);
        } else {
            StringBuilder ingredients = new StringBuilder();


            for (String ingredient : ingredientsList) {
                ingredients.append(ingredient+"\n");
            }
            ingredients.setLength(ingredients.length() - 2);

            mIngredientTv.setText(ingredients);
        }



        String description = sandwich.getDescription();

        if (description.isEmpty()) {
            mDescriptionTv.setText(R.string.detail_error_message);
        } else {
            mDescriptionTv.setText(description);
        }
    }
}
