package by.litvin.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import by.litvin.R;
import by.litvin.activity.CharacterDetailActivity;
import by.litvin.listeners.MoveAndSwipeListener;
import by.litvin.model.Character;
import by.litvin.model.Image;

//TODO try to implement using ListAdapter
public class CharactersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MoveAndSwipeListener {

    public static final int TYPE_NORMAL = 1;
    private static final int TYPE_LOADING = 2;
    public static final String CHARACTER = "character";

    private Context context;
    private List<Character> characters;

    public CharactersRecyclerViewAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }

    public void setCharacterItems(List<Character> characters) {
        this.characters.addAll(characters);

        //TODO not a best way, change later (noitfy insert etc)
        notifyDataSetChanged();
    }

    public void addNullDataForProgressBar() {
        characters.add(null);
        notifyItemInserted(characters.size() - 1);
    }

    public void removeNullDataForProgressBar() {
        characters.remove(characters.size() - 1);
        notifyItemInserted(characters.size());
    }

    public List<Character> getCharacters() {
        return characters;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            CardView characterItem = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_recycler_item, parent, false);
            return new CharacterCardView(characterItem);
        } else {
            View footerProgressBar = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_recycler_footer, parent, false);
            return new FooterView(footerProgressBar);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CharacterCardView) {
            CharacterCardView characterCardView = (CharacterCardView) viewHolder;
            TextView characterName = characterCardView.cardView.findViewById(R.id.character_name);
            TextView characterDescription = characterCardView.cardView.findViewById(R.id.character_description);
            ImageView characterImage = characterCardView.cardView.findViewById(R.id.character_photo);

            Character character = characters.get(position);
            if (character != null) {
                characterName.setText(character.getName());
                characterDescription.setText(character.getDescription());

                Image characterThumbnail = character.getThumbnail();
                if (characterThumbnail != null) {
                    String imageUrl = String.format("%s.%s", characterThumbnail.getPath(), characterThumbnail.getExtension());
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .error(R.mipmap.ic_launcher_round);
                    //TODO load photos of lower resolution for recycler view
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(options)
                            .into(characterImage);
                }
            }

            characterCardView.cardView.setOnClickListener(view -> {
                Intent intent = new Intent(context, CharacterDetailActivity.class);
                intent.putExtra(CHARACTER, character);
                //TODO add image, character name and description to transition animation
                Pair imageTransition = Pair.create(characterImage, context.getString(R.string.character_image_transition_name));
                context.startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation((Activity) context, imageTransition).toBundle());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (characters.get(position) != null) {
            return TYPE_NORMAL;
        } else {
            return TYPE_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //TODO implement item move
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        //TODO do something with swipe logic
        notifyDataSetChanged();
    }

    public class CharacterCardView extends RecyclerView.ViewHolder {
        private CardView cardView;

        public CharacterCardView(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.character_card_view);
        }
    }

    public class FooterView extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public FooterView(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }
}
