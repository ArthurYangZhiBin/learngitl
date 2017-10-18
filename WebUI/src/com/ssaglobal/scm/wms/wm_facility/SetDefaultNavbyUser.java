package com.ssaglobal.scm.wms.wm_facility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.Navigation;
import com.epiphany.shr.metadata.objects.Screen;
import com.epiphany.shr.metadata.objects.generated.np.NavigationFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.login.SSOManager;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.util.UserUtil;

public class SetDefaultNavbyUser extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetDefaultNavbyUser.class);

	protected int execute(ActionContext context, ActionResult result) throws UserException{		

		_log.debug("LOG_DEBUG_EXTENSION_SETDEFAULTNAV","Executing SetDefaultNavbyUser",100L);		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		HttpSession session = state.getRequest().getSession();
		String uid = UserUtil.getUserId(state);
		List profiles = context.getServiceManager().getUserContext().getUserProfileIDs();
		BioCollection defaultCollection = null;
		try {
			Query loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' ", null);	
			_log.debug("LOG_SYSTEM_OUT","wsdefaults.USERID ="+ uid,100L);
			defaultCollection = uow.getBioCollectionBean(loadBiosQry);
			_log.debug("LOG_DEBUG_EXTENSION_SETDEFAULTNAV","Got User Default Collection:"+defaultCollection,100L);			
		} catch (RuntimeException e1) {			
			e1.printStackTrace();
		}
		try {
			Bio bio = defaultCollection.elementAt(0);
			if (bio.get("DEFAULTSCREEN") != null){
				String FavScreen = bio.get("DEFAULTSCREEN").toString().trim();
				//Check Permissions To Make Sure User Can View This Screen
				//Build FAVORITESPROFILEMAPPING table query and get records for this user's profiles
				String dbName = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
				if(!SecurityUtil.isAdmin(state)){
					String bioQryStr = "";
					Iterator profileItr = profiles.iterator();
					while(profileItr.hasNext()){
						if(bioQryStr.length() == 0)
							bioQryStr += "(wm_favoritesprofilemapping.PROFILEID = '"+profileItr.next().toString().toUpperCase()+"' ";
						else
							bioQryStr += " OR wm_favoritesprofilemapping.PROFILEID = '"+profileItr.next().toString().toUpperCase()+"' ";
					}
					bioQryStr += ") AND wm_favoritesprofilemapping.SCREENID = '"+FavScreen+"'";

					if(dbName.equalsIgnoreCase("enterprise"))
						bioQryStr += " AND wm_favoritesprofilemapping.ISENTERPRISE = '1'";
					else
						bioQryStr += " AND wm_favoritesprofilemapping.ISENTERPRISE = '0'";

					_log.debug("LOG_DEBUG_EXTENSION_SETDEFAULTNAV","FAVORITESPROFILEMAPPING QRY:"+bioQryStr,100L);
					BioCollectionBean bioCollectionTemp = uow.getBioCollectionBean(new Query("wm_favoritesprofilemapping",bioQryStr,""));
					try {
						if(bioCollectionTemp == null || bioCollectionTemp.size() == 0){
							String args[] = new String[0];
							String errorMsg = getTextMessage("WMEXP_FAV_INVALID_PERMISSION",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (EpiDataException e) {
						e.printStackTrace();
						return RET_CANCEL;
					}
				}
				String query= null;
				if(!(bio.get("FACILITY") == null || bio.get("FACILITY").toString().trim().length() == 0)){
					String facility = bio.get("FACILITY").toString();
					if(facility.equalsIgnoreCase("enterprise")){
						query = "screens.SCREENCODE = '"+FavScreen+"' AND screens.ISENTERPRISE = '1'";
					}else{
						query = "screens.SCREENCODE = '"+FavScreen+"' AND screens.ISENTERPRISE = '0'";
					}
				}
				Query screenBioQry = new Query("screens", query , null);
				BioCollection ScreenBioCollection = uow.getBioCollectionBean(screenBioQry);
				if (ScreenBioCollection.size() > 0){
					Bio screen = ScreenBioCollection.elementAt(0);
					String defaultNavid = screen.get("DEFAULTNAVID") == null ? null: screen.get("DEFAULTNAVID").toString().trim();
					String linkedBio = screen.get("LINKEDBIO") == null ? null : screen.get("LINKEDBIO").toString().trim();
					String orderBy = screen.get("ORDERBY") == null ? null : screen.get("ORDERBY").toString().trim();
					if (!(linkedBio.equalsIgnoreCase("none"))){
						Query loadBiosQry = new Query(linkedBio,"",orderBy);
						setBC(uow.getBioCollectionBean(loadBiosQry), result);
					}
					Navigation navigation = new NavigationFactory(context.getNavigation().getBaseMetaObject().getMetaFactory()).getById(defaultNavid).getWrapper();
					_log.debug("LOG_DEBUG_EXTENSION_SETDEFAULTNAV","Setting Nav "+navigation.getName(),100L);
					context.setNavigation(navigation.getName());
				}
			}
		}	
		catch (EpiDataException e) {			
			e.printStackTrace();
		}		
		_log.debug("LOG_DEBUG_EXTENSION_SETDEFAULTNAV","Leaving SetDefaultNavbyUser",100L);
		return RET_CONTINUE;

	}	
	private void setBC(BioCollectionBean bioCollection, ActionResult result){
		bioCollection.setEmptyList(true);
		result.setFocus(bioCollection);
	}
}
