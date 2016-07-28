package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
import com.abc.terry_sun.abc.R;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.orm.util.NamingHelper;

import net.glxn.qrgen.android.QRCode;

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
    static int QR_Default_Height=0;
    static int QR_Default_Width=0;
    static boolean IsBiggerQR=false;
    public List<CategoryInfo> GetAllCategory()
    {
        List<CategoryInfo> CategoryInfoList=new ArrayList<CategoryInfo>();
        List<DB_Cards> CardList = DB_Cards.findWithQuery(DB_Cards.class, "Select * from "+ NamingHelper.toSQLName(DB_Cards.class)+" group by CATEGORY_ID,CATEGORY_NAME,CATEGORY_IMAGE");
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
        List<DB_Cards> CardList = DB_Cards.findWithQuery(DB_Cards.class, "Select * from "+NamingHelper.toSQLName(DB_Cards.class)+"  group by GROUP_ID,GROUP_NAME,GROUP_IMAGE");
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
        List<DB_Cards> CardList = DB_Cards.findWithQuery(DB_Cards.class, "Select * from "+NamingHelper.toSQLName(DB_Cards.class)+ " group by GROUP_ID,REPRESENTATIVE_ID,REPRESENTATIVE_NAME,REPRESENTATIVE_IMAGE");
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

    public void RemoveCards(String EntityCardID)
    {
        List<DB_Cards> CardList= DB_Cards.find(DB_Cards.class, "ENTITY_CARD_ID=?", EntityCardID);
        if(CardList.size()>0)
        {
            for(DB_Cards item:CardList) {
                DB_Cards.delete(item);
            }
        }
    }
    public void RemoveFriend(String FriendID)
    {
        List<DB_Friend> FriendList= DB_Friend.find(DB_Friend.class, "FRIEND_ID=?", FriendID);
        if(FriendList.size()>0)
        {
            for(DB_Friend item:FriendList) {
                DB_Friend.delete(item);
            }
        }
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
        DB_Cards.executeQuery("UPDATE "+NamingHelper.toSQLName(DB_Cards.class)+"  set IS_MAIN_CARD=0");
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
        Log.i(TAG,"GetFilteredCards.size()="+ String.valueOf(TempCardList.size()));
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
        List<DB_Events> DataList=DB_Events.find(DB_Events.class, "Card_ID=? and Card_Type='virtual'", CardID);
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
        ShowCardDetailDialog(EntityCardID, Message, context, IsMainCard,null);
    }
    public void ShowCardDetailDialog(final String EntityCardID,final String Message,final Context context,boolean IsMainCard,DB_Events _DB_Events)
    {
        CardDetailDialog=new Dialog(context);
        final DB_Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);

        CardDetailDialog.setContentView(R.layout.dialog_emulator);
        Window window = CardDetailDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ScreenService.GetScreenWidth(), ScreenService.GetScreenHeight());

        CardDetailUISetting(context, SelectedCardInfo);
        CardDetailBonusUISetting(SelectedCardInfo);

        if(_DB_Events!=null)
        {
            //setup bonus detail
            CardDetailBonusDetailUISetting(SelectedCardInfo,_DB_Events);
        }

        if(Message!=null)
        {
            ShowMessage(Message);
        }

        if(IsMainCard) {
            CardDetailDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        CardDetailDialog.show();
    }

    private void CardDetailUISetting(final Context context, final DB_Cards selectedCardInfo) {
        LinearLayout LinearLayout_Background =(LinearLayout)CardDetailDialog.findViewById(R.id.LinearLayout_Background);
        LinearLayout_Background.setBackground(new BitmapDrawable(CardService.getInstance().GetCardImageByCardID(selectedCardInfo.getCardID())));
        //calculate photo height
        int CardWidth = ScreenService.GetScreenWidth();
        int CardHeight=(CardWidth/5)*7;
        LinearLayout_Background.getLayoutParams().height = CardHeight;
        LinearLayout_Background.requestLayout();

        ImageView Button_Exit=(ImageView)CardDetailDialog.findViewById(R.id.Button_Exit);
        Button_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardDetailDialog.dismiss();
            }
        });

        final ImageView ImageView_QR=(ImageView)CardDetailDialog.findViewById(R.id.ImageView_QR);

        QR_Default_Height=ImageView_QR.getLayoutParams().height;
        QR_Default_Width=ImageView_QR.getLayoutParams().width;
        ImageView_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsBiggerQR) {
                    ImageView_QR.getLayoutParams().height = QR_Default_Height;
                    ImageView_QR.getLayoutParams().width = QR_Default_Width;
                    IsBiggerQR = false;
                } else {
                    ImageView_QR.getLayoutParams().height = QR_Default_Height * 3;
                    ImageView_QR.getLayoutParams().width = QR_Default_Width * 3;
                    IsBiggerQR = true;
                }
                ImageView_QR.requestLayout();
            }
        });

        Long TimeStamp = System.currentTimeMillis()/1000;
        Bitmap myBitmap = QRCode.from(String.valueOf(TimeStamp) + "," + selectedCardInfo.getUserCardID()).bitmap();
        ImageView_QR.setImageBitmap(myBitmap);


        final BootstrapButton Button_ShowCard=(BootstrapButton)CardDetailDialog.findViewById(R.id.Button_ShowCard);
        if(selectedCardInfo.getIsMainCard())
        {
            Button_ShowCard.setVisibility(View.GONE);
        }
        else
        {
            Button_ShowCard.setVisibility(View.VISIBLE);
        }
        Button_ShowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardService.getInstance().SetMainCards(selectedCardInfo);
                MainActivity.ChangeMainCardImage(ImageService.GetBitmapFromImageName(selectedCardInfo.getCardImage()), selectedCardInfo.getEntityCardID());
                Button_ShowCard.setVisibility(View.GONE);
                Toast.makeText(context, "This card is Show card now.", Toast.LENGTH_SHORT).show();
            }
        });

        final BootstrapButton Button_FavoriteCard=(BootstrapButton)CardDetailDialog.findViewById(R.id.Button_FavoriteCard);
        if(selectedCardInfo.getIsFavorite())
        {
            Button_FavoriteCard.setBootstrapText(new BootstrapText.Builder(context)
                    .addFontAwesomeIcon("fa_heart")
                    .addText("Favorite")
                    .build());
        }
        else
        {
            Button_FavoriteCard.setBootstrapText(new BootstrapText.Builder(context)
                    .addText("Add to favorite")
                    .build());
        }
        Button_FavoriteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardService.getInstance().ToggleIsCardFavorite(selectedCardInfo);
                if(selectedCardInfo.getIsFavorite())
                {
                    Button_FavoriteCard.setBootstrapText(new BootstrapText.Builder(context)
                            .addFontAwesomeIcon("fa_heart")
                            .addText("Favorite")
                            .build());
                    Toast.makeText(context, "Added to favorites.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Button_FavoriteCard.setBootstrapText(new BootstrapText.Builder(context)
                            .addText("Add to favorite")
                            .build());
                    Toast.makeText(context, "Removed from favorites.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        final BootstrapButton Button_Media=(BootstrapButton)CardDetailDialog.findViewById(R.id.Button_Media);
        Button_Media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardService.getInstance().SetMainCards(selectedCardInfo);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(selectedCardInfo.getRelationLink()));
                context.startActivity(i);
            }
        });


    }

    private void CardDetailBonusUISetting(final DB_Cards selectedCardInfo) {

        final DB_Events EntityCardEvent= CardService.getInstance().GetEntityEventsByCardID(selectedCardInfo.getCardID());
        final DB_Events VirtualCardEvent= CardService.getInstance().GetVirtualEventsByCardID(selectedCardInfo.getCardID());



        final BootstrapButton Button_Bonus=(BootstrapButton)CardDetailDialog.findViewById(R.id.Button_Bonus);
        final FrameLayout FrameLayout_CardBonus=(FrameLayout)CardDetailDialog.findViewById(R.id.FrameLayout_CardBonus);
        Button_Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout FrameLayout_CardBonusDetail=(FrameLayout)CardDetailDialog.findViewById(R.id.FrameLayout_CardBonusDetail);
                FrameLayout_CardBonusDetail.setVisibility(View.GONE);
                FrameLayout_CardBonus.setVisibility(View.VISIBLE);
            }
        });

        final ImageView ImageViewCloseBonus=(ImageView)CardDetailDialog.findViewById(R.id.ImageViewCloseBonus);
        ImageViewCloseBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout_CardBonus.setVisibility(View.GONE);
            }
        });


        LinearLayout LinearLayoutBonus1 = (LinearLayout) FrameLayout_CardBonus.findViewById(R.id.LinearLayoutBonus1);
        LinearLayoutBonus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardDetailBonusDetailUISetting(selectedCardInfo,EntityCardEvent);
            }
        });

        LinearLayout LinearLayoutBonus2 = (LinearLayout) FrameLayout_CardBonus.findViewById(R.id.LinearLayoutBonus2);
        LinearLayoutBonus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardDetailBonusDetailUISetting(selectedCardInfo,VirtualCardEvent);
            }
        });

        ImageButton CardButton = (ImageButton) FrameLayout_CardBonus.findViewById(R.id.CardButton);
        CardButton.setImageBitmap(BitmapFactory.decodeFile(StorageService.GetImagePath(selectedCardInfo.getCardImage())));

        TextView Bonus1_Title = (TextView) FrameLayout_CardBonus.findViewById(R.id.action1_title);
        TextView Bonus1_Title_Point = (TextView) FrameLayout_CardBonus.findViewById(R.id.action1_title_point);
        TextView Bonus1_Title2 = (TextView) FrameLayout_CardBonus.findViewById(R.id.action1_title2);
        TextView Bonus1_Content = (TextView) FrameLayout_CardBonus.findViewById(R.id.action1_content);



        Bonus1_Title.setText("DP[");
        Bonus1_Title_Point.setText(selectedCardInfo.getDirectPoint());
        Bonus1_Title2.setText("/"+EntityCardEvent.getDirectPointTarget()+"]: " + EntityCardEvent.getEventTitle());

        //Bonus1_Content.setText(EntityCardEvent.getEventDescription());
        Bonus1_Content.setText(BonusService.GetBonusLeftDayString(EntityCardEvent)+" days left!");



        TextView Bonus2_Title = (TextView) FrameLayout_CardBonus.findViewById(R.id.action2_title);
        TextView Bonus2_Title_Point = (TextView) FrameLayout_CardBonus.findViewById(R.id.action2_title_point);
        TextView Bonus2_Title2 = (TextView) FrameLayout_CardBonus.findViewById(R.id.action2_title2);
        TextView Bonus2_Content = (TextView) FrameLayout_CardBonus.findViewById(R.id.action2_content);


        Bonus2_Title.setText("IP[");
        Bonus2_Title_Point.setText(selectedCardInfo.getIndirectPoint());



        Bonus2_Title2.setText("/"+EntityCardEvent.getIndirectPointTarget()+"]: "+VirtualCardEvent.getEventTitle());

        //Bonus2_Content.setText(VirtualCardEvent.getEventDescription());
        Bonus2_Content.setText(BonusService.GetBonusLeftDayString(VirtualCardEvent)+" days left!");
        if(VirtualCardEvent==null) {
            Bonus2_Title.setVisibility(View.GONE);
            Bonus2_Content.setVisibility(View.GONE);
        }


    }
    private void CardDetailBonusDetailUISetting(final DB_Cards selectedCardInfo,final DB_Events _DB_Events)
    {
        final FrameLayout FrameLayout_CardBonusDetail=(FrameLayout)CardDetailDialog.findViewById(R.id.FrameLayout_CardBonusDetail);
        FrameLayout_CardBonusDetail.setVisibility(View.VISIBLE);
        final BootstrapButton Button_Bonus=(BootstrapButton)FrameLayout_CardBonusDetail.findViewById(R.id.Button_Award);


        TextView ActionDetail_title = (TextView) FrameLayout_CardBonusDetail.findViewById(R.id.ActionDetail_title);
        TextView ActionDetail_point = (TextView) FrameLayout_CardBonusDetail.findViewById(R.id.ActionDetail_point);
        TextView ActionDetail_title2 = (TextView) FrameLayout_CardBonusDetail.findViewById(R.id.ActionDetail_title2);
        TextView ActionDetail_left_message = (TextView) FrameLayout_CardBonusDetail.findViewById(R.id.ActionDetail_left_message);
        TextView ActionDetail_content = (TextView) FrameLayout_CardBonusDetail.findViewById(R.id.ActionDetail_content);

        final ImageView ImageViewCloseBonusDetail=(ImageView)FrameLayout_CardBonusDetail.findViewById(R.id.ImageViewCloseBonusDetail);
        ImageViewCloseBonusDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout_CardBonusDetail.setVisibility(View.GONE);
            }
        });


        final BootstrapButton ActionDetail_Link=(BootstrapButton)FrameLayout_CardBonusDetail.findViewById(R.id.ActionDetail_Link);
        ActionDetail_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(_DB_Events.getLink()));
                MainActivity.GetMainActivityContext().startActivity(i);
            }
        });
        final BootstrapButton Button_Award=(BootstrapButton)FrameLayout_CardBonusDetail.findViewById(R.id.Button_Award);
        Button_Award.setShowOutline(true);
        boolean OpenAward=false;
        String PointInfo="";
        if(!_DB_Events.getDirectPointTarget().equals("0"))
        {
            ActionDetail_title.setText("DP[");
            ActionDetail_point.setText(selectedCardInfo.getDirectPoint());
            ActionDetail_title2.setText("/"+_DB_Events.getDirectPointTarget()+"]: "+_DB_Events.getEventTitle());
            if(Integer.valueOf(selectedCardInfo.getDirectPoint())>Integer.valueOf(_DB_Events.getDirectPointTarget()))
            {
                OpenAward=true;
            }
        }
        if(!_DB_Events.getIndirectPointTarget().equals("0"))
        {
            ActionDetail_title.setText("IP[");
            ActionDetail_point.setText(selectedCardInfo.getIndirectPoint());
            ActionDetail_title2.setText("/"+_DB_Events.getIndirectPointTarget()+"]: "+_DB_Events.getEventTitle());
            if(Integer.valueOf(selectedCardInfo.getIndirectPoint())>Integer.valueOf(_DB_Events.getIndirectPointTarget()))
            {
                OpenAward=true;
            }
        }

        ActionDetail_left_message.setText(BonusService.GetBonusLeftDayString(_DB_Events)+" days left!");
        ActionDetail_content.setText(_DB_Events.getEventDescription());

        if(OpenAward && !_DB_Events.getHasExchanged())
        {
            Button_Award.setShowOutline(false);
            Button_Award.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button_Award.setText("Coupon:\nABC12313");
                }
            });
        }



    }
    private void ShowMessage(String Message) {
        final View FrameLayout_Message=CardDetailDialog.findViewById(R.id.FrameLayout_Message);
        FrameLayout_Message.setVisibility(View.VISIBLE);

        final ImageView ImageViewCloseMessage=(ImageView)FrameLayout_Message.findViewById(R.id.ImageViewCloseMessage);
        ImageViewCloseMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout_Message.setVisibility(View.GONE);
            }
        });

        final TextView TextViewMessage=(TextView)FrameLayout_Message.findViewById(R.id.TextViewMessage);
        TextViewMessage.setText(Message);
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