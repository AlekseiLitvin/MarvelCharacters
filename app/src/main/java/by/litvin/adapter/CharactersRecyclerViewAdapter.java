package by.litvin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.litvin.R;
import by.litvin.model.Character;

public class CharactersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MoveAndSwipeListener {

    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_FOOTER = 3;

    private Context context;
    private List<Character> characters;

    public CharactersRecyclerViewAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }

    public void setCharacterItems(List<Character> characters) {
        this.characters.addAll(characters);
        notifyDataSetChanged();
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
                characterImage.setImageResource(R.drawable.storm); //TODO add character image fetched from API
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_NORMAL;
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

    public static class CharacterCardView extends RecyclerView.ViewHolder {
        private CardView cardView;

        public CharacterCardView(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.character_card_view);
        }
    }

    public static class FooterView extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public FooterView(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }
}
