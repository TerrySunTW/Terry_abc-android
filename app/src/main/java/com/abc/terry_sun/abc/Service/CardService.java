package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
import com.abc.terry_sun.abc.NFC.NfcStorage;
import com.abc.terry_sun.abc.R;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

/**
 * Created by terry_sun on 2015/7/1.
 */
public class CardService {
    private static final CardService _CardService = new CardService();
    public static CardService getInstance() {
        return _CardService;
    }
    public List<CategoryInfo> GetAllCategory()
    {
        List<CategoryInfo> CategoryInfoList=new ArrayList<CategoryInfo>();
        List<Cards> CardList = Cards.findWithQuery(Cards.class, "Select * from CARDS group by CATEGORY_ID,CATEGORY_NAME,CATEGORY_IMAGE", null);
        for(Cards Item:CardList)
        {
            CategoryInfoList.add(
                    new CategoryInfo(
                            Item.getCategoryID(),
                            Item.getCategoryName(),
                            Item.getCategoryImage()
                    ));
        }
        return CategoryInfoList;
    }
    public List<GroupInfo> GetAllGroup()
    {
        List<GroupInfo> GroupInfoList=new ArrayList<GroupInfo>();
        List<Cards> CardList = Cards.findWithQuery(Cards.class, "Select * from CARDS group by GROUP_ID,GROUP_NAME,GROUP_IMAGE", null);
        for(Cards Item:CardList)
        {
            GroupInfoList.add(
                    new GroupInfo(
                            Item.getCategoryID(),
                            Item.getGroupID(),
                            Item.getGroupName(),
                            Item.getGroupImage()
                    ));
        }
        return GroupInfoList;
    }
    public List<GroupInfo> GetGroupByCategoryID(String CategoryID)
    {
        List<GroupInfo> GroupInfoList=GetAllGroup();
        List<GroupInfo> NewGroupInfoList=new ArrayList<GroupInfo>();
        for(GroupInfo Item:GroupInfoList)
        {
            if(Item.getCategoryID().equals(CategoryID))
            {
                NewGroupInfoList.add(Item);
            }
        }
        return NewGroupInfoList;
    }
    public List<RepresentativeInfo> GetAllRepresentative()
    {
        List<RepresentativeInfo> RepresentativeInfoList=new ArrayList<RepresentativeInfo>();
        List<Cards> CardList = Cards.findWithQuery(Cards.class, "Select * from CARDS group by REPRESENTATIVE_ID,REPRESENTATIVE_NAME,REPRESENTATIVE_IMAGE", null);
        for(Cards Item:CardList)
        {
            RepresentativeInfoList.add(
                    new RepresentativeInfo(
                            Item.getGroupID(),
                            Item.getRepresentativeID(),
                            Item.getRepresentativeName(),
                            Item.getRepresentativeImage()
                    ));
        }
        return RepresentativeInfoList;
    }
    public List<RepresentativeInfo> GetRepresentativeByGroupID(String GroupID)
    {
        List<RepresentativeInfo> RepresentativeInfoList=GetAllRepresentative();
        List<RepresentativeInfo> NewRepresentativeInfoList=new ArrayList<RepresentativeInfo>();
        for(RepresentativeInfo Item:RepresentativeInfoList)
        {
            if(Item.getGroupID().equals(GroupID))
            {
                NewRepresentativeInfoList.add(Item);
            }
        }
        return NewRepresentativeInfoList;
    }
    public List<CardInfo> GetCardsByRepresentativeID(String RepresentativeID)
    {
        List<Cards> CardList=Cards.find(Cards.class, "REPRESENTATIVE_ID=?", RepresentativeID);
        List<CardInfo> CardInfoList=new ArrayList<CardInfo>();
        for(Cards Item:CardList)
        {
            CardInfoList.add(new CardInfo(Item.getRepresentativeID(),Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
        }
        return CardInfoList;
    }
    public List<CardInfo> GetFavoriteCardsInfo()
    {
        List<Cards> CardList=Cards.find(Cards.class, "IS_FAVORITE=?", "1");
        List<CardInfo> CardInfoList=new ArrayList<CardInfo>();
        for(Cards Item:CardList)
        {
            CardInfoList.add(new CardInfo(Item.getRepresentativeID(),Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
        }
        return CardInfoList;
    }
    public List<CardInfo> GetAllCardsInfo()
    {
        List<Cards> CardList=Cards.listAll(Cards.class);
        List<CardInfo> CardInfoList=new ArrayList<CardInfo>();
        for(Cards Item:CardList)
        {
            CardInfoList.add(new CardInfo(Item.getRepresentativeID(),Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
        }
        return CardInfoList;
    }
    public Cards GetCardsByEntityCardID(String EntityCardID)
    {
        List<Cards> CardList=Cards.find(Cards.class, "ENTITY_CARD_ID=?", EntityCardID);
        if(CardList.size()>0)
        {
            return CardList.get(0);
        }
        return null;
    }
    public Cards GetCardsByCardID(String CardID)
    {
        List<Cards> CardList=Cards.find(Cards.class, "CARD_ID=?", CardID);
        if(CardList.size()>0)
        {
            return CardList.get(0);
        }
        return null;
    }
    public Cards GetMainCards()
    {
        List<Cards> CardList=Cards.find(Cards.class, "IS_MAIN_CARD=?", "1");
        if(CardList.size()>0)
        {
            return CardList.get(0);
        }
        CardList=Cards.listAll(Cards.class);
        if(CardList.size()>0)
        {
            Cards.listAll(Cards.class).get(0);
        }
        return null;
    }
    public void SetMainCards(Cards Card)
    {
        List<Cards> CardList= GetAllCards();
        Cards.executeQuery("UPDATE CARDS set IS_MAIN_CARD=0");
        Card.setIsMainCard(true);
        Card.save();
    }
    public void ToggleIsCardFavorite(Cards Card)
    {
        Card.setIsFavorite(!Card.getIsFavorite());
        Card.save();
        ServerCommunicationService.getInstance().ChangeFavoriteCard(Card.getEntityCardID());
    }
    public List<Cards> GetAllCards()
    {
        return Cards.listAll(Cards.class);
    }

    static Dialog CardDetailDialog;
    public void ShowCardDetailDialog(final String EntityCardID,final Context context)
    {
        CardDetailDialog=new Dialog(context);
        final Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);

        CardDetailDialog.setContentView(R.layout.dialog_card_info);
        Window window = CardDetailDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ScreenService.GetScreenWidth(context).x - 100, ScreenService.GetScreenWidth(context).y - 300);


        ImageView _ImageView=(ImageView)CardDetailDialog.findViewById(R.id.ImageView_ItemImage);
        Bitmap Img = BitmapFactory.decodeFile(StorageService.GetImagePath(SelectedCardInfo.getCardImage()));
        _ImageView.setImageBitmap(Img);

        TextView TextView_ItemName=(TextView)CardDetailDialog.findViewById(R.id.TextView_ItemName);
        TextView_ItemName.setText(SelectedCardInfo.getCardName());

        Button Button_RelationURL = (Button)CardDetailDialog.findViewById(R.id.Button_Media);
        Button_RelationURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardService.getInstance().SetMainCards(SelectedCardInfo);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(SelectedCardInfo.getRelationLink()));
                context.startActivity(i);
            }
        });

        final Button Button_MainCard = (Button)CardDetailDialog.findViewById(R.id.Button_MainCard);
        Button_MainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardService.getInstance().SetMainCards(SelectedCardInfo);
                MainActivity.ChangeMainCardImage(ImageService.GetBitmapFromImageName(SelectedCardInfo.getCardImage()), EntityCardID);
            }
        });
        final Button Button_Favorite = (Button)CardDetailDialog.findViewById(R.id.Button_Favorite);
        if(SelectedCardInfo.getIsFavorite())
        {
            Button_Favorite.setText("Remove from favorites");
        }

        Button_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CardService.getInstance().ToggleIsCardFavorite(SelectedCardInfo);
                if(SelectedCardInfo.getIsFavorite())
                {
                    Button_Favorite.setText("Remove from favorites");
                }
                else
                {
                    Button_Favorite.setText("Favorites");
                }
            }
        });




        Button Button_Return = (Button)CardDetailDialog.findViewById(R.id.Button_Return);
        Button_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardDetailDialog.dismiss();
            }
        });

        Button Button_Emulation = (Button)CardDetailDialog.findViewById(R.id.Button_Emulation);
        Button_Emulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NfcStorage.SetAccount(context,SelectedCardInfo.getEntityCardID());
            }
        });
        CardDetailDialog.show();
    }
    public void CloseCardDetailDialog()
    {
        if(CardDetailDialog!=null)
        {
            CardDetailDialog.dismiss();
            CardDetailDialog=null;
        }
    }
}