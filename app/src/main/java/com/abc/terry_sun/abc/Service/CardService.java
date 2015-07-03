package com.abc.terry_sun.abc.Service;

import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
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
    public List<Cards> GetAllCards()
    {
        return Cards.listAll(Cards.class);
    }
}