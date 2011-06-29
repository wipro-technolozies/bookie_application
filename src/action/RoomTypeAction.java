package action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Facility;
import model.Image;

import model.RoomType;
import model.Structure;
import model.User;
import model.internal.Message;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import service.FacilityService;
import service.ImageService;
import service.RoomTypeService;
import service.StructureService;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage(value="default")
public class RoomTypeAction extends ActionSupport implements SessionAware{
	private Map<String, Object> session = null;
	private Message message = new Message();
	private List<RoomType> roomTypes;
	private RoomType roomType = null;
	private Image image = null;
	private List<Facility> facilities = null;
	private List<Integer> roomTypeFacilitiesIds = new ArrayList<Integer>();
	@Autowired
	private StructureService structureService = null;
	@Autowired
	private RoomTypeService roomTypeService;
	@Autowired
	private ImageService imageService = null;
	@Autowired
	private FacilityService facilityService = null;
	
	
	@Actions({
		@Action(value="/findAllRoomTypes",results = {
				@Result(name="success",location="/roomTypes.jsp")
		})
	})
	public String findAllRoomTypes() {
		User user = null;
		Structure structure = null;
		List<RoomType> roomTypes = null;
		
		user = (User)this.getSession().get("user");
		structure = user.getStructure();
		roomTypes = this.getRoomTypeService().findRoomTypesByIdStructure(structure);
		for(RoomType each: roomTypes){
			each.setImages(this.getImageService().findImagesByIdRoomType(each.getId()));
		}
		this.setRoomTypes(roomTypes);
		this.setFacilities(this.getStructureService().findRoomFacilitiesByIdStructure(structure));
		return SUCCESS;
	}
	
	@Actions({
		@Action(value="/goUpdateRoomType",results = {
				@Result(name="success",location="/roomType_edit.jsp")
		})		
	})
	public String goUpdateRoomType() {
		User user = null;
		Structure structure = null;
		RoomType roomType = null;
		
		user = (User)this.getSession().get("user");
		structure = user.getStructure();
		
		roomType = this.getRoomTypeService().findRoomTypeById(structure,this.getRoomType().getId());
		roomType.setImages(this.getImageService().findImagesByIdRoomType(this.getRoomType().getId()));
		this.setRoomType(roomType);
		this.setFacilities(this.getFacilityService().findUploadedFacilitiesByIdStructure(structure.getId()));
		for(Facility each: this.getRoomType().getFacilities()){			
			this.getRoomTypeFacilitiesIds().add(each.getId());		//popolo l'array roomFacilitiesIds con gli id delle Facilities già presenti nella Room da editare
		}
		return SUCCESS;
	}
	
	@Actions({
		@Action(value="/saveUpdateRoomType",results = {
				@Result(type ="json",name="success", params={
						"root","message"
				})
		})
	})
	public String saveUpdateRoomType(){
		User user = null;
		Structure structure = null;
		RoomType oldRoomtype = null;
		List<Facility> checkedFacilities = null;
		
		user = (User)session.get("user");
		structure = user.getStructure();
		
		//checkedFacilities = this.getStructureService().findRoomFacilitiesByIds(structure,this.getRoomTypeFacilitiesIds());
		checkedFacilities = this.getFacilityService().findUploadedFacilitiesByIds(this.getRoomTypeFacilitiesIds());
		oldRoomtype = this.getRoomTypeService().findRoomTypeById(structure,this.getRoomType().getId());
		if(oldRoomtype == null){
			//Si tratta di una aggiunta
			this.getRoomType().setId(structure.nextKey());
			this.getRoomType().setFacilities(checkedFacilities);
			this.getRoomTypeService().insertRoomType(structure, this.getRoomType());
			this.getFacilityService().insertRoomTypeFacilities(this.getRoomTypeFacilitiesIds(), this.getRoomType().getId());
			this.getStructureService().refreshPriceLists(structure);
			this.getMessage().setResult(Message.SUCCESS);
			this.getMessage().setDescription(getText("roomTypeAddSuccessAction"));
			
		}else{
			//Si tratta di un update
			this.getRoomType().setFacilities(checkedFacilities);
			this.getRoomTypeService().updateRoomType(structure, this.getRoomType());
			this.getFacilityService().deleteAllFacilitiesFromRoomType(this.getRoomType().getId());
			this.getFacilityService().insertRoomTypeFacilities(this.getRoomTypeFacilitiesIds(), this.getRoomType().getId());
			this.getMessage().setResult(Message.SUCCESS);
			this.getMessage().setDescription(getText("roomTypeUpdateSuccessAction"));
		}
		return SUCCESS;		
	}
	
	@Actions({
		@Action(value="/deleteRoomType",results = {
				@Result(type ="json",name="success", params={
						"root","message"
				}),
				@Result(type ="json",name="error", params={
						"root","message"
				})
		})
	})
	public String deleteRoomType(){
		User user = null;
		Structure structure = null;
		RoomType currentRoomType = null;
		
		user = (User)this.getSession().get("user");
		structure = user.getStructure();
		currentRoomType = this.getRoomTypeService().findRoomTypeById(structure,this.getRoomType().getId());
		if(this.getRoomTypeService().removeRoomType(structure, currentRoomType) >0){
			this.getMessage().setResult(Message.SUCCESS);
			this.getMessage().setDescription(getText("roomTypeDeleteSuccessAction"));
			return SUCCESS;
		}else{
			this.getMessage().setResult(Message.ERROR);
			this.getMessage().setDescription(getText("roomTypeDeleteErrorAction"));
			return ERROR;
		}
	}
		
	@Actions({
		@Action(value="/deletePhotoRoomType",results = {
				@Result(type ="json",name="success", params={
						"root","message"
				}),
				@Result(type ="json",name="error", params={
						"root","message"
				})
		})
	})
	public String deletePhotoRoomType() {
		User user = null; 
		user = (User)this.getSession().get("user");
				
		if(this.getImageService().deleteRoomTypeImage(this.getImage().getId())>0){
			this.getMessage().setResult(Message.SUCCESS);
			this.getMessage().setDescription(getText("roomTypeImageDeleteSuccessAction"));
			return "success";
		}else{
			this.getMessage().setResult(Message.ERROR);
			this.getMessage().setDescription(getText("roomTypeImageDeleteErrorAction"));
			return "error";
		}		
	}
	
	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;		
	}	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}
	public void setRoomTypes(List<RoomType> roomTypes) {
		this.roomTypes = roomTypes;
	}
	public RoomType getRoomType() {
		return roomType;
	}
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public List<Integer> getRoomTypeFacilitiesIds() {
		return roomTypeFacilitiesIds;
	}
	public void setRoomTypeFacilitiesIds(List<Integer> roomFacilitiesIds) {
		this.roomTypeFacilitiesIds = roomFacilitiesIds;
	}	
	public List<Facility> getFacilities() {
		return facilities;
	}
	public void setFacilities(List<Facility> roomTypeFacilities) {
		this.facilities = roomTypeFacilities;
	}
	public StructureService getStructureService() {
		return structureService;
	}
	public void setStructureService(StructureService structureService) {
		this.structureService = structureService;
	}
	public RoomTypeService getRoomTypeService() {
		return roomTypeService;
	}
	public void setRoomTypeService(RoomTypeService roomTypeService) {
		this.roomTypeService = roomTypeService;
	}

	public ImageService getImageService() {
		return imageService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public FacilityService getFacilityService() {
		return facilityService;
	}

	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	
}