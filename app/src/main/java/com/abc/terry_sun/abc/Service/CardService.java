package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
import com.abc.terry_sun.abc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by terry_sun on 2015/7/1.
 */
public class CardService {
    private static final CardService _CardService = new CardService();
    static String TAG="CardService";
    public static CardService getInstance() {
        return _CardService;
    }
    public List<CategoryInfo> GetAllCategory()
    {
        List<CategoryInfo> CategoryInfoList=new ArrayList<CategoryInfo>();
        List<DB_Cards> CardList = DB_Cards.findWithQuery(DB_Cards.class, "Select * from "+DB_Cards.getTableName(DB_Cards.class)+" group by CATEGORY_ID,CATEGORY_NAME,CATEGORY_IMAGE", null);
        for(DB_Cards Item:CardList)
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
        List<DB_Cards> CardList = DB_Cards.findWithQuery(DB_Cards.class, "Select * from "+DB_Cards.getTableName(DB_Cards.class)+"  group by GROUP_ID,GROUP_NAME,GROUP_IMAGE", null);
        for(DB_Cards Item:CardList)
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
        List<DB_Cards> CardList = DB_Cards.findWithQuery(DB_Cards.class, "Select * from "+DB_Cards.getTableName(DB_Cards.class)+ " group by GROUP_ID,REPRESENTATIVE_ID,REPRESENTATIVE_NAME,REPRESENTATIVE_IMAGE", null);
        for(DB_Cards Item:CardList)
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
    public List<CardInfo> GetCardsByConditions(String CategoryID,String GroupID,String RepresentativeID)
    {
        List<DB_Cards> CardList;
        if(CategoryID.length()>0) {
            CardList = DB_Cards.find(DB_Cards.class, "(?='' or Category_ID=?) and (?='' or Group_ID=?) and (?='' or REPRESENTATIVE_ID=?)",
                    CategoryID,CategoryID, GroupID,GroupID, RepresentativeID,RepresentativeID);
        }
        else
        {
            CardList = DB_Cards.listAll(DB_Cards.class);
        }
        List<CardInfo> CardInfoList=new ArrayList<CardInfo>();
        for(DB_Cards Item:CardList)
        {
            CardInfo _CardInfo=new CardInfo(
                    Item.getRepresentativeID(),
                    Item.getEntityCardID(),
                    Item.getCardName(),Item.getCardImage(),
                    Item.getHasRealCard()
            );
            if(!CardInfoList.contains(_CardInfo)) {
                CardInfoList.add(_CardInfo);
            }
        }
        return CardInfoList;
    }
    public List<CardInfo> GetCardsByRepresentativeID(String GroupID,String RepresentativeID)
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "Group_ID=? and REPRESENTATIVE_ID=?", GroupID, RepresentativeID);
        List<CardInfo> CardInfoList=new ArrayList<CardInfo>();
        for(DB_Cards Item:CardList)
        {
            CardInfoList.add(new CardInfo(
                    Item.getRepresentativeID(),
                    Item.getEntityCardID(),
                    Item.getCardName(),Item.getCardImage(),
                    Item.getHasRealCard()
                    ));
        }
        return CardInfoList;
    }
    public List<CardInfo> GetFavoriteCardsInfo()
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "IS_FAVORITE=?", "1");
        List<CardInfo> CardInfoList=new ArrayList<CardInfo>();
        for(DB_Cards Item:CardList)
        {
            CardInfo _CardInfo=new CardInfo(
                    Item.getRepresentativeID(),
                    Item.getEntityCardID(),
                    Item.getCardName(),
                    Item.getCardImage(),
                    Item.getHasRealCard()
            );
            if(!CardInfoList.contains(_CardInfo))
            {
                CardInfoList.add(_CardInfo);
            }
        }
        return CardInfoList;
    }
    public List<CardInfo> GetAllCardsInfo()
    {
        List<DB_Cards> CardList = DB_Cards.listAll(DB_Cards.class);
        List<CardInfo> CardInfoList=new ArrayList<CardInfo>();
        for(DB_Cards Item:CardList)
        {
            CardInfoList.add(new CardInfo(Item.getRepresentativeID(),Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
        }
        return CardInfoList;
    }
    public DB_Cards GetUserOwnCardsByEntityCardID(String EntityCardID)
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "ENTITY_CARD_ID=? and Introducer_ID=Owner_ID", EntityCardID);
        if(CardList.size()>0)
        {
            return CardList.get(0);
        }
        return null;
    }
    public DB_Cards GetCardsByEntityCardID(String EntityCardID)
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "ENTITY_CARD_ID=?", EntityCardID);
        if(CardList.size()>0)
        {
            return CardList.get(0);
        }
        return null;
    }
    public DB_Cards GetCardsByUserCardID(String UserCardID)
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "USER_CARD_ID=?", UserCardID);
        if(CardList.size()>0)
        {
            return CardList.get(0);
        }
        return null;
    }
    public DB_Cards GetCardsByCardID(String CardID)
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "CARD_ID=?", CardID);
        if(CardList.size()>0)
        {
            return CardList.get(0);
        }
        return null;
    }
    public DB_Cards GetMainCards()
    {
        try {
            List<DB_Cards> CardList = DB_Cards.find(DB_Cards.class, "IS_MAIN_CARD=?", "1");
            if (CardList.size() > 0) {
                return CardList.get(0);
            }
            CardList = DB_Cards.listAll(DB_Cards.class);
            if (CardList.size() > 0) {
                DB_Cards.listAll(DB_Cards.class).get(0);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG,ex.getStackTrace().toString());
        }
        return null;
    }
    public void SetMainCards(DB_Cards Card)
    {
        List<DB_Cards> CardList= GetAllCards();
        DB_Cards.executeQuery("UPDATE "+DB_Cards.getTableName(DB_Cards.class)+"  set IS_MAIN_CARD=0");
        Card.setIsMainCard(true);
        Card.save();
        ServerCommunicationService.getInstance().SetMainCard(Card.getEntityCardID());
    }
    public void ToggleIsCardFavorite(DB_Cards Card)
    {
        Card.setIsFavorite(!Card.getIsFavorite());
        Card.save();
        ServerCommunicationService.getInstance().ChangeFavoriteCard(Card.getEntityCardID());
    }
    public List<DB_Cards> GetAllCards()
    {
        List<DB_Cards> AllCardList = DB_Cards.listAll(DB_Cards.class);
        List<DB_Cards> NewCardList = new ArrayList<DB_Cards>();
        List<String> CardIDList = new ArrayList<String>();
        for(DB_Cards item:AllCardList) {
            if(!CardIDList.contains(item.getCardID()))
            {
                CardIDList.add(item.getCardID());
                NewCardList.add(item);
            }
        }
        return NewCardList;
    }
    public List<DB_Cards> GetFilteredCards(String CategoryID,String GroupID,String RepresentativeID)
    {
        List<DB_Cards> CardList=GetAllCards();
        List<DB_Cards> TempCardList=new ArrayList<DB_Cards>();

        for(DB_Cards item:CardList) {
            if((CategoryID.equals("")||CategoryID.equals(item.getCategoryID()))&&
                    (GroupID.equals("")||GroupID.equals(item.getGroupID()))&&
                    (RepresentativeID.equals("")||RepresentativeID.equals(item.getRepresentativeID()))
                    )
            {
                TempCardList.add(item);
            }
        }

        return TempCardList;
    }
    public List<DB_Events> GetAllEvents()
    {
        return DB_Events.listAll(DB_Events.class);
    }

    public DB_Events GetEntityEventsByCardID(String CardID)
    {
        List<DB_Events> DataList=DB_Events.find(DB_Events.class, "Card_ID=? and Card_Type='entity'", CardID);
        if(DataList.size()>0)
        {
            return DataList.get(0);
        }
        return null;
    }
    public DB_Events GetVirtualEventsByCardID(String CardID)
    {
        List<DB_Events> DataList=DB_Events.find(DB_Events.class, "Card_ID=? and Card_Type='vitual'", CardID);
        if(DataList.size()>0)
        {
            return DataList.get(0);
        }
        return null;
    }
    public List<DB_Cards> GetFavorateCards()
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "IS_FAVORITE=?", "1");
        List<DB_Cards> NewCardList = new ArrayList<DB_Cards>();
        List<String> CardIDList = new ArrayList<String>();
        for(DB_Cards item:CardList) {
            if(!CardIDList.contains(item.getCardID()))
            {
                CardIDList.add(item.getCardID());
                NewCardList.add(item);
            }
        }
        return NewCardList;
    }
    public Boolean CheckCardIDIsFavorate(String CardID)
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "Card_ID=?", CardID);
        if(CardList.size()>0)
        {
            return CardList.get(0).getIsFavorite();
        }
        return false;
    }
    public List<DB_Events> GetFavorateEvents()
    {
        List<DB_Cards> favorateCards =GetFavorateCards();
        List<DB_Events> EventList=GetAllEvents();
        List<DB_Events> TempEventList=new ArrayList<DB_Events>();
        for(DB_Events item:EventList) {
            if (CheckCardIDIsFavorate(item.getCardID())) {
                TempEventList.add(item);
            }
        }
        return TempEventList;
    }
    //option would be "", when user didn't select anything
    public List<DB_Events> GetFilteredEvents(String CategoryID,String GroupID,String RepresentativeID)
    {
        List<DB_Events> EventList=GetAllEvents();
        List<DB_Events> TempEventList=new ArrayList<DB_Events>();

        for(DB_Events item:EventList) {
            if((CategoryID.equals("")||CategoryID.equals(item.getCategoryID()))&&
                (GroupID.equals("")||GroupID.equals(item.getGroupID()))&&
                (RepresentativeID.equals("")||RepresentativeID.equals(item.getRepresentativeID()))
            )
            {
                TempEventList.add(item);
            }
        }

        return TempEventList;
    }
    public void RemoveAllEvents()
    {
        DB_Events.deleteAll(DB_Events.class);
    }
    public void RemoveAllFriends()
    {
        DB_Friend.deleteAll(DB_Friend.class);
    }
    static Dialog CardDetailDialog;
    public void ShowCardDetailDialog(final String EntityCardID,final String Message,final Context context)
    {
        ShowCardDetailDialog(EntityCardID, Message, context,false);
    }
    public void ShowCardDetailDialog(final String EntityCardID,final String Message,final Context context,boolean IsMainCard)
    {
        if(CardDetailDialog!=null)
        {
            CardDetailDialog.dismiss();
        }
        CardDetailDialog=new Dialog(context);
        final DB_Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);
        if(SelectedCardInfo==null)
        {
            Log.e("SelectedCardInfo","SelectedCardInfo is null");
            return;
        }
        CardDetailDialog.setContentView(R.layout.dialog_card_info);
        Window window = CardDetailDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ScreenService.GetScreenWidth(context).x - 100, ScreenService.GetScreenWidth(context).y - 300);


        TextView TextView_Message=(TextView)CardDetailDialog.findViewById(R.id.TextView_Message);
        if(Message !=null)
        {
            TextView_Message.setVisibility(View.VISIBLE);
            TextView_Message.setText(Message);
        }
        else
        {
            TextView_Message.setVisibility(View.GONE);
            //extView_Message.setText("");
        }



        ImageView _ImageView=(ImageView)CardDetailDialog.findViewById(R.id.ImageView_ItemImage);
        Bitmap Img = BitmapFactory.decodeFile(StorageService.GetImagePath(SelectedCardInfo.getCardImage()));
        _ImageView.setImageBitmap(Img);
        _ImageView.setTag(SelectedCardInfo.getEntityCardID());
        _ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardDetailDialog.dismiss();
                EmulatorService.getInstance().ShowEmulatorDialog(view.getTag().toString());
            }
        });

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
            Button_Favorite.setText("(X) Favorites");
        }

        Button_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CardService.getInstance().ToggleIsCardFavorite(SelectedCardInfo);
                if(SelectedCardInfo.getIsFavorite())
                {
                    Button_Favorite.setText("(X) Favorites");
                }
                else
                {
                    Button_Favorite.setText("Favorites");
                }
            }
        });

        Button Button_Bonus = (Button)CardDetailDialog.findViewById(R.id.Button_Bonus);
        Button_Bonus.setTag(SelectedCardInfo.getEntityCardID());
        Button_Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardDetailDialog.dismiss();
                BonusService.getInstance().ShowAllBonusDialog(view.getTag().toString());
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
        Button_Emulation.setTag(SelectedCardInfo.getEntityCardID());
        Button_Emulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardDetailDialog.dismiss();
                EmulatorService.getInstance().ShowEmulatorDialog(view.getTag().toString());
            }
        });
        if(IsMainCard) {
            CardDetailDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
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
    public Bitmap GetCardImageByCardID(String CardID)
    {
        DB_Cards card=CardService.getInstance().GetCardsByCardID(CardID);
        return BitmapFactory.decodeFile(StorageService.GetImagePath(card.getCardImage()));
    }
    public String GetEntityCardIDByCardID(String CardID)
    {
        DB_Cards card=CardService.getInstance().GetCardsByCardID(CardID);
        return card.getEntityCardID();
    }
    public String GetUserCardIDByCardID(String CardID)
    {
        DB_Cards card=CardService.getInstance().GetCardsByCardID(CardID);
        return card.getUserCardID();
    }
}