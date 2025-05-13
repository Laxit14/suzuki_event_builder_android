package com.multitv.eventbuilder.apimethods


import EntertainmentResponse
import com.multitv.eventbuilder.model.ResponseNotification
import com.multitv.eventbuilder.model.aboutdoha.model.AboutDohaResponse
import com.multitv.eventbuilder.model.askmodel.model.AskQuestionResponse
import com.multitv.eventbuilder.model.award.model.AwardResponse
import com.multitv.eventbuilder.model.conference.model.ConferenceDataResponse
import com.multitv.eventbuilder.model.confrence_detail.model.ConferenceAgendaDataResponse
import com.multitv.eventbuilder.model.confrence_detail.model.DetailAgendaResponse
import com.multitv.eventbuilder.model.confrence_detail.model.ShuttleBusAgendaResponse
import com.multitv.eventbuilder.model.confrence_detail.model.SpouseTourResponse
import com.multitv.eventbuilder.model.currencymodel.model.CurrencyConversionResponse
import com.multitv.eventbuilder.model.digitalexhibitionfeedbackmodel.model.DigitalExhibitionFeedbackResponse
import com.multitv.eventbuilder.model.digitalexhibitionmodel.model.DigitalExhibitionResponse
import com.multitv.eventbuilder.model.dodonotmodel.model.DoDontResponse
import com.multitv.eventbuilder.model.dresscode.model.DressCodeResponse
import com.multitv.eventbuilder.model.emergencymodel.EmergencyResponse
import com.multitv.eventbuilder.model.eventlocation.model.EventLocationResponse
import com.multitv.eventbuilder.model.feedbackmodel.model.FeedbackResponse
import com.multitv.eventbuilder.model.fooddetailmodel.model.FoodScheduleResponse
import com.multitv.eventbuilder.model.homemodel.model.HomeDataItem
import com.multitv.eventbuilder.model.hotelstaymodel.model.HotelStayResponse
import com.multitv.eventbuilder.model.interactionmodel.model.InteractionResponse
import com.multitv.eventbuilder.model.lamplightingmodel.PartsResponse
import com.multitv.eventbuilder.model.localizationmodel.model.LocalizationResponse
import com.multitv.eventbuilder.model.loginimage.LoginImage
import com.multitv.eventbuilder.model.loginmodel.model.LoginResponse
import com.multitv.eventbuilder.model.msilcontact.model.MsilContactResponse
import com.multitv.eventbuilder.model.mynotesmodel.model.MYNotesResponse
import com.multitv.eventbuilder.model.otpmodel.model.OtpResponse
import com.multitv.eventbuilder.model.ourspeaker.model.OurSpeakerResponse
import com.multitv.eventbuilder.model.seatingplanmodel.model.SeatResponse
import com.multitv.eventbuilder.model.traveldetailmodel.model.TravelDetailResponse
import com.multitv.eventbuilder.model.travelstay.model.TravelAndStayResponse
import com.multitv.eventbuilder.model.weathermodel.model.WeatherResponse
import com.multitv.eventbuilder.model.welcomemsvcmodel.model.WelcomeMessageResponse
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.model.PostNotesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface AllApiMethods {


    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("mobile") mobile: String,
        @Field("token") token: String,
    ): retrofit2.Response<LoginResponse>

    @FormUrlEncoded
    @POST("verify_otp.php")
    suspend fun verifyOtp(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String
    ): retrofit2.Response<OtpResponse>

    @GET
    suspend fun getHomeData(
        @Url url: String
    ): retrofit2.Response<HomeDataItem>

    /*@GET("conference_page.php")
    suspend fun getConferenceData(
        @Query("id") id : Long,
        @Query("parent_id") parentId : Long
     ) : retrofit2.Response<ConferenceDataResponse>*/

    @GET
    suspend fun getConferenceData(
        @Url url: String
    ): retrofit2.Response<ConferenceDataResponse>


    @GET("hotel_stay.php")
    suspend fun getTravelAndStayData(
        @Query("hotel_stay_id") hotelStayId: Int,
        @Query("category") category: String? = null
    ): retrofit2.Response<TravelAndStayResponse>


    @GET("conference_agenda.php")
    suspend fun getConferenceAgendaData(
        @Query("id") id: String,
        @Query("app_id") appId: String
    )


    /* *//* use speaker_id = 5 *//*
    @GET("our_speaker.php")
    suspend fun getSpeakerData(
        @Query("speaker_id") speakerId : String
    )*/

    /*  use seat_id = 6*/
    @GET
    suspend fun getSeatingPlanData(
        @Url url: String,
        @Query("user_id") userId: String
    ): retrofit2.Response<SeatResponse>

    /*  use dress_id = 7*/
    @GET
    suspend fun getDressCodeData(
        @Url url: String
    ): retrofit2.Response<DressCodeResponse>

    @GET
    suspend fun getMsilContact(
        @Url url: String
    ): retrofit2.Response<MsilContactResponse>

    @GET
    suspend fun getWelcomeMessageData(
        @Url url: String
    ): retrofit2.Response<WelcomeMessageResponse>


    @GET
    suspend fun getAboutDohaData(
        @Url url: String
    ): retrofit2.Response<AboutDohaResponse>

    @GET
    suspend fun getHotelStayData(
        @Url url: String
    ): retrofit2.Response<HotelStayResponse>

    @GET
    suspend fun getWeatherData(
        @Url url: String
    ): retrofit2.Response<WeatherResponse>

    /* @GET
     suspend fun getEventLocationData(
         @Url url : String
     ) : retrofit2.Response<WeatherResponse>*/

    @FormUrlEncoded
    @POST
    suspend fun submitQuestion(
        @Url url: String,
        @Field("user_id") userId: Long,
        @Field("question") question: String,
        @Field("event_id") eventId: String,
        @Field("audi_id") audiId: String
    ): retrofit2.Response<AskQuestionResponse>


    @GET
    suspend fun getShuttleBusDetailData(
        @Url url: String
    ): retrofit2.Response<ShuttleBusAgendaResponse>

    @GET
    suspend fun getSpouseTourData(
        @Url url: String
    ): retrofit2.Response<SpouseTourResponse>

    @GET
    suspend fun getDetailAgendaData(
        @Url url: String
    ): retrofit2.Response<DetailAgendaResponse>

    @GET
    suspend fun getConferenceAgenda(
        @Url url: String
    ): retrofit2.Response<ConferenceAgendaDataResponse>

    @GET
    suspend fun getTravelDetail(
        @Url url: String
    ): retrofit2.Response<TravelDetailResponse>

    @GET
    suspend fun getDoDoNotResponse(
        @Url url: String
    ): retrofit2.Response<DoDontResponse>

    @GET
    suspend fun getFoodPlan(
        @Url url: String
    ): retrofit2.Response<FoodScheduleResponse>

    @FormUrlEncoded
    @POST
    suspend fun submitFeedback(
        @Url url: String,
        @Field("hotel_accomodation_star") hotel_accomodation_star: Float,
        @Field("hotel_accomodation_comment") hotel_accomodation_comment: String,
        @Field("entertainment_star") entertainment_star: Float,
        @Field("entertainment_comment") entertainment_comment: String,
        @Field("engagement_star") engagement_star: Float,
        @Field("engagement_comment") engagement_comment: String,
        @Field("food_star") food_star: Float,
        @Field("food_comment") food_comment: String,
        @Field("overall_exper_star") overall_exper_star: Float,
        @Field("overall_exper_comment") overall_exper_comment: String,
        @Field("user_id") audiId: String
    ): retrofit2.Response<FeedbackResponse>

    @GET
    suspend fun getAwardsData(
        @Url url: String,
        @Query("user_id") userId: Long
    ): retrofit2.Response<AwardResponse>


    @GET
    suspend fun getLocalizationData(
        @Url url: String,
    ): retrofit2.Response<LocalizationResponse>

    @GET
    suspend fun getCurrencyConvertor(
        @Url url: String,
    ): retrofit2.Response<CurrencyConversionResponse>

    @GET
    suspend fun getSpeakerData(
        @Url url: String
    ): retrofit2.Response<OurSpeakerResponse>

    @FormUrlEncoded
    @POST
    suspend fun postNotes(
        @Url url: String,
        @Field("speaker_id") speakerId: Int,
        @Field("user_id") userId: Long,
        @Field("content") content: String,
        @Field("tittle") tittle: String
    ): retrofit2.Response<PostNotesResponse>


    @GET
    suspend fun getInteractionResponse(
        @Url url: String
    ): retrofit2.Response<InteractionResponse>

    @GET
    suspend fun getDigitalExhibitionResponse(
        @Url url: String,
        @Query("user_id") userId: Long
    ): retrofit2.Response<DigitalExhibitionResponse>

    @GET
    suspend fun getMyAllNotes(
        @Url url: String,
        @Query("user_id") userId: Long
    ): retrofit2.Response<MYNotesResponse>

    @GET
    suspend fun getEventLocationData(
        @Url url: String
    ): retrofit2.Response<EventLocationResponse>


    @FormUrlEncoded
    @POST
    suspend fun submitDigitalExhibitionFeedback(
        @Url url: String,
        @Field("user_id") userId: Long,
        @Field("overall_feedback") overallFeedback: String,
        @Field("expectations") expectations: String,
        @Field("organization_benefit") organizationBenefit: String,
        @Field("content_feedback") contentFeedback: String,
        @Field("organization_reason") organizationReason: String,
        @Field("study_area") studyArea: String,
        @Field("digilib_comment") digilibComment: String
    ): retrofit2.Response<DigitalExhibitionFeedbackResponse>


    @GET
    suspend fun getLampLighting(
        @Url url: String
    ): retrofit2.Response<PartsResponse>

    @GET
    suspend fun getEmergencyData(
        @Url url: String
    ): retrofit2.Response<EmergencyResponse>


    @GET
    suspend fun getNotificationData(
        @Url url: String
    ): retrofit2.Response<ResponseNotification>

    @Multipart
    @POST
    suspend fun uploadImage(
        @Url url: String,
        @Part("id") id: RequestBody,
        @Part upload_image: MultipartBody.Part
    ): retrofit2.Response<LoginImage>

    @GET
    suspend fun getEntertainment(
        @Url url: String
    ): retrofit2.Response<EntertainmentResponse>

}