package by.litvin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import by.litvin.R;
import by.litvin.model.Character;
import by.litvin.model.Image;

public class FavCharactersRecyclerViewAdapter extends ListAdapter<Character, FavCharactersRecyclerViewAdapter.CharacterHolder> {


    public FavCharactersRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Character> DIFF_CALLBACK = new DiffUtil.ItemCallback<Character>() {
        @Override
        public boolean areItemsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.character_recycler_item, parent, false);
        return new CharacterHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterHolder holder, int position) {
        Character character = getItem(position);
        holder.characterName.setText(character.getName());
        holder.characterDescription.setText(character.getDescription());

        Image characterThumbnail = character.getThumbnail();
        if (characterThumbnail != null) {
            String imageUrl = String.format("%s.%s", characterThumbnail.getPath(), characterThumbnail.getExtension());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.ic_launcher_round);
            //TODO load photos of lower resolution for recycler view
            ImageView characterImage = holder.characterImage;
            Glide.with(characterImage.getContext())
                    .load(imageUrl)
                    .apply(options)
                    .into(characterImage);
        }

    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        TextView characterName;
        TextView characterDescription;
        ImageView characterImage;

        public CharacterHolder(@NonNull View itemView) {
            super(itemView);
            characterName = itemView.findViewById(R.id.character_name);
            characterDescription = itemView.findViewById(R.id.character_description);
            characterImage = itemView.findViewById(R.id.character_photo);
            itemView.setOnClickListener(view -> {
                //TODO intent to CharacterDetailActivity
            });
        }
    }
}
