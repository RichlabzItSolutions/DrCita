package com.drcita.user.retrofit;

import androidx.activity.result.ActivityResultCallerKt;

import com.drcita.user.models.GlobalRequest;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.ads.AdResponse;
import com.drcita.user.models.ambulance.AmbulanceRequest;
import com.drcita.user.models.appointment.AppointmentListResponse;
import com.drcita.user.models.appointment.AppointmentRequest;
import com.drcita.user.models.appointment.AppointmentResponse;
import com.drcita.user.models.appointment.BookAppointmentRequest;
import com.drcita.user.models.appointment.CAncelAppointmentRequest;
import com.drcita.user.models.appointment.DocterDetailResoponse;
import com.drcita.user.models.appointment.DocterDetailsRequest;
import com.drcita.user.models.appointment.ResechudleAppointmentRequest;
import com.drcita.user.models.appointment.singleAppointmentRequest;
import com.drcita.user.models.appointmentbookingsummary.AppointmentsBookingResponse;
import com.drcita.user.models.appointmentbookingsummary.CancelReasonResponse;
import com.drcita.user.models.appointmentbookingsummary.CancelRequest;
import com.drcita.user.models.cities.CityRequestData;
import com.drcita.user.models.cities.CityResponse;
import com.drcita.user.models.contact.ContactRequest;
import com.drcita.user.models.departments.DepartmentResponse;
import com.drcita.user.models.departments.SpecializationRequest;
import com.drcita.user.models.departments.SpecializationResponse;
import com.drcita.user.models.doctorDetails.DoctorRequest;
import com.drcita.user.models.doctorDetails.DoctorResponse;
import com.drcita.user.models.doctors.DoctorsListResponse;
import com.drcita.user.models.doctorslots.BookAppRequest;
import com.drcita.user.models.doctorslots.DoctorslotsListRequest;
import com.drcita.user.models.doctorslots.DoctorslotsListResponse;
import com.drcita.user.models.doctorslots.SlotBookingNumberRequest;
import com.drcita.user.models.doctorslots.SlotResponse;
import com.drcita.user.models.forgotpassword.ForgotPasswordRequest;
import com.drcita.user.models.forgotpassword.ForgotPasswordResponse;
import com.drcita.user.models.home.HomeDataRequest;
import com.drcita.user.models.home.HomeResponse;
import com.drcita.user.models.home.SearchRequest;
import com.drcita.user.models.home.SearchResponse;
import com.drcita.user.models.hospitalreviews.HospitalReviewRequest;
import com.drcita.user.models.hospitalreviews.HospitalReviewResponse;
import com.drcita.user.models.hospitals.HospitalsRequest;
import com.drcita.user.models.hospitals.HospitalsResponse;
import com.drcita.user.models.hospitals.SingleDocterRequest;
import com.drcita.user.models.hospitals.SingleDoctorResponse;
import com.drcita.user.models.language.UpdateLanguageRequest;
import com.drcita.user.models.login.LoginRequest;
import com.drcita.user.models.login.LoginResponse;
import com.drcita.user.models.medicalrecords.GetMedicalRecordsRequest;
import com.drcita.user.models.medicalrecords.GetMedicalRecordsResponse;
import com.drcita.user.models.newProviderlist.ProviderResponse;
import com.drcita.user.models.newProviderlist.ProvidersRequestData;
import com.drcita.user.models.notifications.NotificationResponse;
import com.drcita.user.models.otp.OtpResponse;
import com.drcita.user.models.otp.VerifyotpRequest;
import com.drcita.user.models.otp.VerifyotpResponse;
import com.drcita.user.models.payment.PaymentRequest;
import com.drcita.user.models.profile.AddNewMember;
import com.drcita.user.models.profile.AddProfile;
import com.drcita.user.models.profile.BloodGroupResponse;
import com.drcita.user.models.profile.CheckUserRequest;
import com.drcita.user.models.profile.CheckUserResponse;
import com.drcita.user.models.profile.DiseaseResponse;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.GetProfileResponse;
import com.drcita.user.models.profile.ProfileResponse;
import com.drcita.user.models.profile.RelationResponse;
import com.drcita.user.models.profile.UpdateMemberRequest;
import com.drcita.user.models.profile.UpdateNameRequest;
import com.drcita.user.models.profile.UpdateNameResponse;
import com.drcita.user.models.profile.UpdateProfileRequest;
import com.drcita.user.models.profile.UpdateProfileResponse;
import com.drcita.user.models.region.Response;
import com.drcita.user.models.region.UpdateRegionRequest;
import com.drcita.user.models.resetpassword.ChangePasswodRequest;
import com.drcita.user.models.resetpassword.ResetPasswordRequest;
import com.drcita.user.models.resetpassword.ResetPasswordResponse;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.models.review.WriteReviewRequest;
import com.drcita.user.models.scans.DiagnosticsRequest;
import com.drcita.user.models.scans.DiagnosticsResponse;
import com.drcita.user.models.scans.ScansListRequest;
import com.drcita.user.models.scans.ScansListResponse;
import com.drcita.user.models.signup.SignupRequest;
import com.drcita.user.models.signup.SignupResponse;
import com.drcita.user.models.specalities.SpecalitiesResponse;
import com.drcita.user.models.specilization.DocterModelResponse;
import com.drcita.user.models.specilization.DoctorSearchRequest;
import com.drcita.user.models.states.StateRequest;
import com.drcita.user.models.states.StateResponse;
import com.drcita.user.models.systemcharges.SystemchargesResponse;
import com.drcita.user.models.userlocation.UpdateUserLocationRequestData;
import com.drcita.user.models.userprofile.MemberResponse;
import com.drcita.user.models.userprofile.MemeberProfileRequest;
import com.drcita.user.models.userprofile.MemeberRequest;
import com.drcita.user.models.userprofile.ProfileDiseaseResponse;
import com.drcita.user.models.userprofile.UserRequestData;
import com.drcita.user.models.userprofile.UsersResponse;
import com.drcita.user.models.viewmedicalrecords.ViewmedicalRequest;
import com.drcita.user.models.viewmedicalrecords.ViewmedicalResponse;
import com.drcita.user.models.viewreceipt.ViewreceiptRequest;
import com.drcita.user.models.viewreceipt.ViewreceiptResponse;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

//    @POST("user/registerUserNew")
//    Call<SignupResponse>register(@Body SignupRequest signupRequest);

    @POST("user/registerUserNewMogadishu")
    Call<SignupResponse> register(@Body SignupRequest signupRequest);


//
//    @POST("user/verifyOTP")
//    Call<VerifyotpResponse>verifyOTP(@Body VerifyotpRequest verifyotpRequest);

    @POST("user/fetchHomePage")
    Call<HomeResponse> getHomedata(@Body HomeDataRequest dataRequest);


    @POST("user/verifyOTPMogadishu")
    Call<VerifyotpResponse> verifyOTP(@Body VerifyotpRequest verifyotpRequest);

//    @POST("user/loginUser")
//    Call<LoginResponse>loginUser(@Body LoginRequest loginRequest);

//   @POST("user/loginUserMogadishu")
//   Call<LoginResponse>loginUser(@Body LoginRequest loginRequest);

    @POST("user/forgotPassword")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST("user/resetPassword")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("user/city")
    Call<Response> city(@Body GlobalRequest globalRequest);

    @POST("user/cityMogadishu")
    Call<Response> cityMogadishu(@Body GlobalRequest globalRequest);

    @POST("user/userProfileUpdate")
    Call<UpdateProfileResponse> userProfileUpdate(@Body UpdateProfileRequest updateProfileRequest);

    @Multipart
    @POST("user/uploadPic")
    Call<UpdateNameResponse> uploadPic(@Part("data") RequestBody json, @Part MultipartBody.Part picture);

    @POST("user/getUserProfile")
    Call<GetProfileResponse> getUserProfile(@Body GetProfileRequest getProfileRequest);

    @POST("user/updateName")
    Call<UpdateNameResponse> updateName(@Body UpdateNameRequest updateNameRequest);

    @POST("user/updateLanguage")
    Call<GlobalResponse> updateLanguage(@Body UpdateLanguageRequest updateLanguageRequest);

    @POST("user/getProvidersList")
    Call<HospitalsResponse> getProvidersList(@Body HospitalsRequest hospitalsRequest);

    @POST("user/writeProviderReview")
    Call<GlobalResponse> writeProviderReview(@Body WriteReviewRequest writeReviewRequest);

    @POST("user/getProviderReviews")
    Call<HospitalReviewResponse> getProviderReviews(@Body HospitalReviewRequest hospitalReviewRequest);

    @POST("user/getDoctorsList")
    Call<DoctorsListResponse> getDoctorsList(@Body HospitalReviewRequest hospitalReviewRequest);

    @POST("user/getDoctorSlots")
    Call<DoctorslotsListResponse> getDoctorSlots(@Body DoctorslotsListRequest doctorslotsListRequest);

    @POST("user/getSlotNumber")
    Call<SlotResponse> getSlotNumber(@Body SlotBookingNumberRequest slotBookingNumberRequest);

    @POST("user/bookAppointment")
    Call<SlotResponse> bookAppointment(@Body BookAppRequest bookAppRequest);

    @POST("user/updateRegion")
    Call<GlobalResponse> updateRegion(@Body UpdateRegionRequest updateRegionRequest);

//    @POST("user/payNow")
//    Call<GlobalResponse>payNow(@Body PaymentRequest paymentRequest);

    @POST("user/payNowMogadishu")
    Call<GlobalResponse> payNow(@Body PaymentRequest paymentRequest);

    @POST("user/getUserAppointments")
    Call<AppointmentListResponse> getUserAppointments(@Body AppointmentRequest appointmentRequest);

    @POST("user/contactRequest")
    Call<GlobalResponse> contactRequest(@Body ContactRequest contactRequest);

    @POST("user/changePassword")
    Call<GlobalResponse> changePassword(@Body ChangePasswodRequest changePasswodRequest);

    @POST("user/requestAmbulance")
    Call<GlobalResponse> requestAmbulance(@Body AmbulanceRequest ambulanceRequest);

    @POST("user/getUserAppointmentDetails")
    Call<ViewreceiptResponse> viewReceipt(@Body ViewreceiptRequest viewreceiptRequest);

    @GET("user/userAds")
    Call<AdResponse> getAds();

    @POST("user/specialities")
    Call<SpecalitiesResponse> specialities(@Body GlobalRequest globalRequest);

    @POST("user/getUserNotifications")
    Call<NotificationResponse> getNotifications(@Body AppointmentRequest appointmentRequest);

    @POST("user/getScanProvidersList")
    Call<DiagnosticsResponse> getScanProvidersList(@Body DiagnosticsRequest diagnosticsRequest);

    @POST("user/getScansList")
    Call<ScansListResponse> getScansList(@Body ScansListRequest scansListRequest);

    @POST("user/cancelAppointment")
    Call<GlobalResponse> cancelAppointment(@Body CAncelAppointmentRequest cAncelAppointmentRequest);

    @POST("user/fetchMedicalRecordsNew")
    Call<GetMedicalRecordsResponse> getUserMedicalRecords(@Body GetMedicalRecordsRequest getMedicalRecordsRequest);

    @POST("user/getMedicalRecord")
    Call<ViewmedicalResponse> getMedicalRecord(@Body ViewmedicalRequest viewmedicalRequest);

    @POST("user/getDoctorDetails")
    Call<DoctorResponse> getDoctorDetails(@Body DoctorRequest doctorRequest);

    @GET("user/systemCharges")
    Call<SystemchargesResponse> systemCharges();


    // newly added

    @POST("user/getProvidersListNew")
    Call<ProviderResponse> getProviderResponse(@Body ProvidersRequestData requestData);

    @POST("user/loginNew")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("user/verifyOtpNew")
    Call<OtpResponse> verifyOtp(@Body VerifyotpRequest request);


    @POST("user/fetchStates")
    Call<StateResponse> getStates(@Body StateRequest stateRequest);

    @POST("user/fetchCities")
    Call<CityResponse> getCities(@Body CityRequestData cityRequestData);

    @POST("user/updateLocation")
    Call<RestResponse> updateUserLocation(@Body UpdateUserLocationRequestData requestData);


    @POST("user/getDoctorsListNew")
    Call<DocterModelResponse> getDocterList(@Body DoctorSearchRequest request);

    @POST("user/getDoctorDetailsNew")
    Call<DocterDetailResoponse> getdocterDetails(@Body DocterDetailsRequest response);
    @POST("user/fetchUsersNew")
    Call<UsersResponse> getUserProfile(@Body UserRequestData requestData);

    @POST("user/bookAppointmentNew")
    Call<AppointmentResponse> bookAppointment(@Body BookAppointmentRequest appointmentRequest);

    @POST("user/updateMemberNew")
    Call<RestResponse>  updateMember(@Body UpdateMemberRequest request);

    @POST("user/addMemberNew")
    Call<RestResponse> addMemberNew(@Body AddNewMember request);

    @GET("user/fetchRelations")
    Call<RelationResponse> getRelationships();

    @GET("user/fetchDiseases")
    Call<DiseaseResponse> getDiseaseResponse();

    @POST("user/updateProfileNew")

    Call<RestResponse> addProfile(@Body AddProfile addProfile);

    @GET("user/fetchBloodGroups")
    Call<BloodGroupResponse> fetchBloodgroups();

    @POST("user/getProfileNew")

    Call<ProfileResponse> getProfile(@Body GetProfileRequest data);


    @POST("user/fetchMembers")
    Call<MemberResponse> getMembers(@Body MemeberRequest request);

    @POST("user/fetchMemberDetailsNew")
    Call<ProfileDiseaseResponse> getProfileMember(@Body MemeberProfileRequest request);

    @POST("user/checkUserNew")
    Call<CheckUserResponse> checkUser(@Body CheckUserRequest request);

    @POST("user/getAppointmentsNew")
    Call<AppointmentsBookingResponse> getBookingAppointment(@Body GlobalRequest request);
    @GET("user/fetchReasonsForUser")
    Call<CancelReasonResponse> getCancelReason();

    @POST("user/cancelAppointmentNew")
    Call<RestResponse> cancelAppointment(@Body CancelRequest request);

    @POST("user/getAppointmentNew")
    Call<DocterDetailResoponse> getBookedAppointments(@Body singleAppointmentRequest response);

    @POST("user/resheduleAppointment")

    Call<AppointmentResponse> bookAppointment(@Body ResechudleAppointmentRequest appointmentRequest);


    @POST("user/fetchAllMembers")
    Call<MemberResponse> getFetchAll(@Body MemeberRequest request);

    @Multipart
    @POST("user/addMedicalRecordNew")
    Call<RestResponse> uploadMedicalRecord(
            @Part MultipartBody.Part data,
            @Part List<MultipartBody.Part> images,
            @Part List<MultipartBody.Part> pdfs
    );
    @POST("user/searchHomePage")
    Call<SearchResponse> searchHomePage(@Body SearchRequest searhRequest);

    @GET("user/fetchDepartments")
    Call<DepartmentResponse> getDepartments();

    @POST("user/fetchSpecializations")
    Call<SpecializationResponse> getSpecoilizationByDepartment(@Body SpecializationRequest request);


}