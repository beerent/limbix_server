package com.remindme.server.response;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.remindme.reminder.Reminder;
import com.remindme.reminder.Tag;

public class ResponseManager {

	public RequestResponse invalidCredientials() {
		return createErrorResponse("Invalid username password combo.");
	}

	public RequestResponse missingUsername() {
		return createErrorResponse("Request is missing username.");
	}
	
	public RequestResponse missingPassword() {
		return createErrorResponse("Request is missing password.");
	}
	public RequestResponse missingPassword2() {
		return createErrorResponse("Request is missing second password.");
	}
	
	public RequestResponse missingFirstName(){
		return createErrorResponse("Request is missing first name.");
	}
	
	public RequestResponse missingLastName(){
		return createErrorResponse("Request is missing last name.");
	}
	
	public RequestResponse missingEmail(){
		return createErrorResponse("Request is missing email address.");
	}
	
	public RequestResponse missingRequestType() {
		return createErrorResponse("Request is missing request type.");
	}
	
	public RequestResponse invalidRequestType() {
		return createErrorResponse("Request contains an invalid request type.");
	}
	
	public RequestResponse missingReminder() {
		return createErrorResponse("Request is missing reminder.");
	}
	
	public RequestResponse invalidReminder() {
		return createErrorResponse("Request contains an invalid reminder.");
	}
	
	public RequestResponse missingRequiredField() {
		return createErrorResponse("Request is missing one or more required fields.");
	}

	public RequestResponse invalidTag(String tag) {
		return createErrorResponse("Tag '" + tag + "' doesn't exist.");
	}
	
	public RequestResponse multipleDueDateConstraints() {
		return createErrorResponse("Too many Due Date constraints set. Only one allowed.");
	}
	
	public RequestResponse multipleCreatedDateConstraints() {
		return createErrorResponse("Too many Created Date constraints set. Only one allowed.");
	}
	
	public RequestResponse tooManyRemindersFound() {
		return createErrorResponse("Too many Reminders found. Please limit your search.");
	}
	
	public RequestResponse usernameTooShort() {
		return createErrorResponse("Username must have at least 3 characters.");
	}
	
	public RequestResponse usernameTooLong() {
		return createErrorResponse("Username must have at most 20 characters.");
	}
	
	public RequestResponse invalidUsername() {
		return createErrorResponse("Username is invalid. Characters must be alphanumeric or the character _.");
	}
	
	public RequestResponse usernameMissingAlphaCharacter() {
		return createErrorResponse("Username must contain atleast one alpha character.");
	}
	
	public RequestResponse usernameAlreadyExists() {
		return createErrorResponse("Username already exists.");
	}
	
	public RequestResponse passwordTooShort() {
		return createErrorResponse("Password must have at least 8 characters.");
	}
	
	public RequestResponse passwordTooLong() {
		return createErrorResponse("Password must have at most 25 characters.");
	}
	
	public RequestResponse passwordMissingAlphaCharacter() {
		return createErrorResponse("Password must contain atleast one alpha character.");
	}
	
	public RequestResponse passwordsDoNotMatch() {
		return createErrorResponse("Password fields must match.");
	}
	
	public RequestResponse passwordMissingNumericCharacter() {
		return createErrorResponse("Password must contain atleast one numeric character.");
	}
	
	public RequestResponse firstNameTooShort() {
		return createErrorResponse("First name must have at least 1 character.");
	}
	
	public RequestResponse lastNameTooShort() {
		return createErrorResponse("Last name must have at least 1 character.");
	}
	
	public RequestResponse firstNameTooLong() {
		return createErrorResponse("First name must have at most 50 characters.");
	}
	
	public RequestResponse lastNameTooLong() {
		return createErrorResponse("Last name must have at most 50 characters.");
	}
	
	public RequestResponse invalidFirstName() {
		return createErrorResponse("First name is invalid. Characters must be alphanumeric or the characters _ and -.");
	}
	
	public RequestResponse invalidLastName() {
		return createErrorResponse("Last name is invalid. Characters must be alphanumeric or the characters _ and -.");
	}
	
	public RequestResponse invalidEmail() {
		return createErrorResponse("Email address is invalid.");
	}
	
	public RequestResponse reminderTooLong() {
		return createErrorResponse("Reminder is too long. Reminder can have a max of 300 characters.");
	}

	public RequestResponse nothingToUpdate() {
		return createErrorResponse("Request contains no updatable fields to update.");
	}
	
	public RequestResponse noFiltersToAdd() {
		return createErrorResponse("Request contains no filters to add.");
	}

	public RequestResponse invalidReminderId() {
		return createErrorResponse("Invalid Reminder ID or no reminder ID found.");
	}
	
	public RequestResponse reminderIdDoesNotExistForUser() {
		return createErrorResponse("User does not have a reminder with the requested Reminder ID.");
	}
	
	public RequestResponse reminderTooShort() {
		return createErrorResponse("Reminder must contain at least one character.");
	}
	
	public RequestResponse unableToUpdateReminder() {
		return createErrorResponse("There was a problem updating your reminder.");
	}
	
	public RequestResponse unableToUpdateUser() {
		return createErrorResponse("There was a problem updating your user profile.");
	}
	
	public RequestResponse registerUserFailed() {
		return createErrorResponse("There was a problem registering this account.");
	}
	
	public RequestResponse missingSecondNewPassword() {
		return createErrorResponse("Request is missing second new password to update password.");
	}
	
	public RequestResponse missingFirstNewPassword() {
		return createErrorResponse("Request is missing first new password to update password.");
	}
	
	public RequestResponse invalidTags() {
		return createErrorResponse("invalid tags found.");
	}
	
	public RequestResponse nameTooShort() {
		return createErrorResponse("Invalid first or last name. must have at least 1 character.");
	}
	
	public RequestResponse nameTooLong() {
		return createErrorResponse("Invalid first or last name. must have at most 50 characters.");
	}

	public RequestResponse invalidName() {
		return createErrorResponse("Name is invalid. Characters must be alphanumeric or the characters '_' and '-'.");
	}
	
	public RequestResponse missingComplete() {
		return createErrorResponse("Get Tags request requires complete field.");
	}
	
	public RequestResponse missingDeleted() {
		return createErrorResponse("Get Tags request requires deleted field.");
	}
	
	public RequestResponse unknownError() {
		return createErrorResponse("Request contains an unknown error. The error has been recorded.");
	}
	
	public RequestResponse updateUserSuccess() {
		return createSuccessResponse("Update to user profile was successful");
	}
	public RequestResponse updateReminderSuccess() {
		return createSuccessResponse("Update to reminder was successful.");
	}
	
	public RequestResponse addReminderSuccess() {
		return createSuccessResponse("Reminder is successful.");
	}
	
	public RequestResponse registerUserSuccess() {
		return createSuccessResponse("User has successfully registered.");
	}
	
	public RequestResponse loginUserSuccess() {
		return createSuccessResponse("User has successfully logged in.");
	}
	
	public RequestResponse getRemindersSuccess(ArrayList<Reminder> reminders){
		return new RequestResponse(getRemindersJSON(reminders));
	}
	
	public RequestResponse getTagsSuccess(ArrayList<Tag> tags) {
		return new RequestResponse(getTagsJSON(tags));
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getRemindersJSON(ArrayList<Reminder> reminders){
		JSONObject return_json = new JSONObject();
		JSONArray reminders_array = new JSONArray();
		for(Reminder reminder : reminders){
			reminders_array.add(getReminderJSON(reminder));
		}
		return_json.put("reminders", reminders_array);
		return return_json;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getReminderJSON(Reminder reminder){
		JSONObject reminder_json = new JSONObject();
		
		reminder_json.put("reminder_id", "" + reminder.getReminderId());
		reminder_json.put("reminder", "" + reminder.getReminder());
		reminder_json.put("complete", "" + reminder.isComplete());
		reminder_json.put("due_date", "" + reminder.getDueDate());
		reminder_json.put("created_date", "" + reminder.getCreated());
		return reminder_json;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getTagsJSON(ArrayList<Tag> tags){
		JSONObject return_json = new JSONObject();
		JSONArray tag_array = new JSONArray();
		for(Tag tag : tags){
			tag_array.add(getTagJSON(tag));
		}
		return_json.put("tags", tag_array);
		return return_json;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getTagJSON(Tag tag){
		JSONObject tag_json = new JSONObject();
		
		tag_json.put("tag_id", "" + tag.getTagId());
		tag_json.put("tag", "" + tag.getTag());

		return tag_json;
	}

	@SuppressWarnings("unchecked")
	private RequestResponse createSuccessResponse(String message){
		JSONObject response = new JSONObject();
		response.put("op", 0);
		response.put("success", message);
		JSONObject jo = new JSONObject();
		jo.put("response", response);
		return new RequestResponse(jo);
	}
	
	@SuppressWarnings("unchecked")
	private RequestResponse createErrorResponse(String error){
		JSONObject response = new JSONObject();
		response.put("op", 1);
		response.put("error", error);
		JSONObject jo = new JSONObject();
		jo.put("response", response);
		return new RequestResponse(jo);
	}
}
