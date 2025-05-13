package com.multitv.eventbuilder.Repository

import EntertainmentResponse
import android.util.Log
import com.multitv.eventbuilder.apimethods.AllApiMethods
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
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.model.PostNotesResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Repo(private val apiService: AllApiMethods) {


    suspend fun getLogin(mobile: String, token: String): Generic<LoginResponse> {
        return try {
            val response = apiService.login(mobile, token)
            if (response.isSuccessful && response.body() != null) {
                Log.e("request url", "" + response.raw().request)
                Log.e("response code", "" + response.code())
                Log.e("response code", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Generic.Failure(e)
        }
    }

    suspend fun verifyOtp(mobile: String, otp: String): Generic<OtpResponse> {
        return try {
            val response = apiService.verifyOtp(mobile, otp)
            if (response.isSuccessful && response.body() != null) {
                Log.e("request url", "" + response.raw().request)
                Log.e("response code", "" + response.code())
                Log.e("response code", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Generic.Failure(e)
        }
    }

    suspend fun getHomeData(url: String): Generic<HomeDataItem> {

        return try {
            val response = apiService.getHomeData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("home request url", "" + response.raw().request)
                Log.e("home response code", "" + response.code())
                Log.e("home response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Generic.Failure(e)
        }
    }

    suspend fun postQuestion(
        url: String,
        userId: Long,
        question: String,
        eventId: String,
        audiId: String
    ): Generic<AskQuestionResponse> {

        return try {
            val response = apiService.submitQuestion(url, userId, question, eventId, audiId)
            if (response.isSuccessful && response.body() != null) {
                Log.e("postquestion request url", "" + response.raw().request)
                Log.e("postquestion response code", "" + response.code())
                Log.e("postquestion response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Generic.Failure(e)
        }
    }


    suspend fun getWelcomeData(url: String): Generic<WelcomeMessageResponse> {

        return try {
            val response = apiService.getWelcomeMessageData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("welcome request url", "" + response.raw().request).toString()
                Log.e("welcome response code", "" + response.code())
                Log.e("welcome response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("welcome failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getConferenceData(url: String): Generic<ConferenceDataResponse> {
        return try {
            val response = apiService.getConferenceData(url)
            if (response.isSuccessful && response.body() != null) {

                Log.e("conference request url", "" + response.raw().request)
                Log.e("conference response code", "" + response.code())
                Log.e("conference response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("conference failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getTravelAndStayData(hotelStayId: Int,category: String? = null): Generic<TravelAndStayResponse> {

        return try {
            val response = apiService.getTravelAndStayData(hotelStayId,category)
            if (response.isSuccessful && response.body() != null) {
                Log.e("travelstay request url", "" + response.raw().request)
                Log.e("travelstay response code", "" + response.code())
                Log.e("travelstay response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("travelStay failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getAboutDohaData(url: String): Generic<AboutDohaResponse> {
        return try {
            val response = apiService.getAboutDohaData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("About doha request url", "" + response.raw().request)
                Log.e("About doha response code", "" + response.code())
                Log.e("About doha response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("About doha error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("about doha failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getHotelStayData(url: String): Generic<HotelStayResponse> {
        return try {
            val response = apiService.getHotelStayData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("hotelstay request url", "" + response.raw().request)
                Log.e("hotelstay response code", "" + response.code())
                Log.e("hotelstay response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("hotelStay error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("hotelStay failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getWetherData(url: String): Generic<WeatherResponse> {
        return try {
            val response = apiService.getWeatherData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("hotelstay request url", "" + response.raw().request)
                Log.e("hotelstay response code", "" + response.code())
                Log.e("hotelstay response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("hotelStay error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("hotelStay failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getSeatResponse(url: String, userId: String): Generic<SeatResponse> {
        return try {
            val response = apiService.getSeatingPlanData(url, userId)
            if (response.isSuccessful && response.body() != null) {
                Log.e("hotelstay request url", "" + response.raw().request)
                Log.e("hotelstay response code", "" + response.code())
                Log.e("hotelstay response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("hotelStay error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("hotelStay failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getDressCodeData(url: String): Generic<DressCodeResponse> {
        return try {
            val response = apiService.getDressCodeData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("hotelstay request url", "" + response.raw().request)
                Log.e("hotelstay response code", "" + response.code())
                Log.e("hotelstay response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("hotelStay error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("hotelStay failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getMsilContactData(url: String): Generic<MsilContactResponse> {
        return try {
            val response = apiService.getMsilContact(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("hotelstay request url", "" + response.raw().request)
                Log.e("hotelstay response code", "" + response.code())
                Log.e("hotelstay response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("hotelStay error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("hotelStay failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getShuttleBusDetailData(url: String): Generic<ShuttleBusAgendaResponse> {
        return try {
            val response = apiService.getShuttleBusDetailData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("shuttle request url", "" + response.raw().request)
                Log.e("shuttlebus response code", "" + response.code())
                Log.e("shuttlebus response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("shuttlebus error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("shuttlebus failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getSpouseTourData(url: String): Generic<SpouseTourResponse> {
        return try {
            val response = apiService.getSpouseTourData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("spouseTour request url", "" + response.raw().request)
                Log.e("spouseTour response code", "" + response.code())
                Log.e("spouseTour response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("spouseTour error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("spouseTour failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getDetailAgendaData(url: String): Generic<DetailAgendaResponse> {
        return try {
            val response = apiService.getDetailAgendaData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("detailAgenda request url", "" + response.raw().request)
                Log.e("detailAgenda response code", "" + response.code())
                Log.e("detailAgenda response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("detailAgenda error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("detailAgenda failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getConferenceAgenda(url: String): Generic<ConferenceAgendaDataResponse> {
        return try {
            val response = apiService.getConferenceAgenda(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("conferenceAgenda request url", "" + response.raw().request)
                Log.e("conferenceAgenda response code", "" + response.code())
                Log.e("conferenceAgenda response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("conferenceAgenda error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("conferenceAgenda failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getTravelDetailResponse(url: String): Generic<TravelDetailResponse> {
        return try {
            val response = apiService.getTravelDetail(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("travelDetail request url", "" + response.raw().request)
                Log.e("travelDetail response code", "" + response.code())
                Log.e("travelDetail response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("travelDetail error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("travelDetail failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getDoDoNotResponse(url: String): Generic<DoDontResponse> {
        return try {
            val response = apiService.getDoDoNotResponse(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("dodonot request url", "" + response.raw().request)
                Log.e("dodonot response code", "" + response.code())
                Log.e("dodonot response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("dodonot error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("dodonot failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getFoodPlan(url: String): Generic<FoodScheduleResponse> {
        return try {
            val response = apiService.getFoodPlan(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("foodplan request url", "" + response.raw().request)
                Log.e("foodplan response code", "" + response.code())
                Log.e("foodplan response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("foodplan error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("foodplan failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getFeedbackResponse(
        url: String, hotel_star: Float, hotel_comment: String, entertnmt_str: Float,
        entertnmt_cmnt: String,
        engagemnt_str: Float, engagmnt_cmnt: String, food_str: Float, food_cmnt: String,
        overral_str: Float, overall_cmt: String, userId: String
    ): Generic<FeedbackResponse> {
        return try {
            val response = apiService.submitFeedback(
                url, hotel_star, hotel_comment, entertnmt_str, entertnmt_cmnt,
                engagemnt_str, engagmnt_cmnt, food_str, food_cmnt, overral_str, overall_cmt, userId
            )
            if (response.isSuccessful && response.body() != null) {
                Log.e("feedback request url", "" + response.raw().request)
                Log.e("feedback response code", "" + response.code())
                Log.e("feedback response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("feedback error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("feedback failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getAwardsResponse(url: String, userId: Long): Generic<AwardResponse> {
        return try {
            val response = apiService.getAwardsData(url, userId)
            if (response.isSuccessful && response.body() != null) {
                Log.e("Awards request url", "" + response.raw().request)
                Log.e("Awards response code", "" + response.code())
                Log.e("Awards response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("Awards error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("Awards failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getLocalizationData(url: String): Generic<LocalizationResponse> {
        return try {
            val response = apiService.getLocalizationData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("Localization request url", "" + response.raw().request)
                Log.e("Localization response code", "" + response.code())
                Log.e("Localization response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("Localization error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("Localization failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getCurrencyConvertor(url: String): Generic<CurrencyConversionResponse> {
        return try {
            val response = apiService.getCurrencyConvertor(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("Localization request url", "" + response.raw().request)
                Log.e("Localization response code", "" + response.code())
                Log.e("Localization response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("Localization error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("Localization failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getSpeakerData(url: String): Generic<OurSpeakerResponse> {
        return try {
            val response = apiService.getSpeakerData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("ourSpeaker request url", "" + response.raw().request)
                Log.e("ourSpeaker response code", "" + response.code())
                Log.e("ourSpeaker response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("ourSpeaker error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("ourSpeaker failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun postNotes(
        url: String,
        speakerId: Int,
        userId: Long,
        content: String,
        tittle: String
    ): Generic<PostNotesResponse> {
        return try {
            val response = apiService.postNotes(url, speakerId, userId, content, tittle)
            if (response.isSuccessful && response.body() != null) {
                Log.e("postNotes request url", "" + response.raw().request)
                Log.e("postNotes response code", "" + response.code())
                Log.e("postNotes response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("postNotes error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("postNotes failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getInteractionData(url: String): Generic<InteractionResponse> {
        return try {
            val response = apiService.getInteractionResponse(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("Interaction request url", "" + response.raw().request)
                Log.e("Interaction response code", "" + response.code())
                Log.e("Interaction response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("Interaction error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("Interaction failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getDigitalExhibitionData(
        url: String,
        userId: Long
    ): Generic<DigitalExhibitionResponse> {
        return try {
            val response = apiService.getDigitalExhibitionResponse(url, userId)
            if (response.isSuccessful && response.body() != null) {
                Log.e("DigitalExhibition request url", "" + response.raw().request)
                Log.e("DigitalExhibition response code", "" + response.code())
                Log.e("DigitalExhibition response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("DigitalExhibition error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("DigitalExhibition failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getMyAllNotsData(url: String, userId: Long): Generic<MYNotesResponse> {
        return try {
            val response = apiService.getMyAllNotes(url, userId)
            if (response.isSuccessful && response.body() != null) {
                Log.e("AllNots request url", "" + response.raw().request)
                Log.e("AllNots response code", "" + response.code())
                Log.e("AllNots response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("AllNots error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("AllNots failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getEventLocationResponse(url: String): Generic<EventLocationResponse> {
        return try {
            val response = apiService.getEventLocationData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("eventLocation request url", "" + response.raw().request)
                Log.e("eventLocation response code", "" + response.code())
                Log.e("eventLocation response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("eventLocation error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("eventLocation failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getDigitalExhibitionFeedbackResponse(
        url: String, userId: Long, overallFeedback: String, content_feedback: String,
        organization_reason: String, organization_benefit: String,
        expectations: String, study_area: String, digilib_comment: String
    ): Generic<DigitalExhibitionFeedbackResponse> {
        return try {
            val response = apiService.submitDigitalExhibitionFeedback(
                url,
                userId,
                overallFeedback,
                content_feedback,
                organization_benefit,
                organization_reason,
                expectations,
                study_area,
                digilib_comment
            )
            if (response.isSuccessful && response.body() != null) {
                Log.e("DigitalExhibitionfeedback request url", "" + response.raw().request)
                Log.e("DigitalExhibitionfeedback response code", "" + response.code())
                Log.e("DigitalExhibitionfeedback response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("DigitalExhibitionfeedback error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("DigitalExhibitionfeedback failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


    suspend fun getLampLightingResponse(url: String): Generic<PartsResponse> {
        return try {
            val response = apiService.getLampLighting(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("lamplight request url", "" + response.raw().request)
                Log.e("lamplight response code", "" + response.code())
                Log.e("lamplight response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("lamplight error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("lamplight failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    suspend fun getEmergencyResponse(url: String): Generic<EmergencyResponse> {

        return try {
            val response = apiService.getEmergencyData(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("emergency request url", "" + response.raw().request).toString()
                Log.e("emergency response code", "" + response.code())
                Log.e("emergency response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("emergency failure exception", "" + e.message)
            Generic.Failure(e)
        }


    }

    suspend fun getNotificationResponse(url: String): Generic<ResponseNotification> {
        return try {
            val response = apiService.getNotificationData(url)
            if (response.isSuccessful && response.body() != null) {
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Server error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("emergency failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }

    /*  suspend fun uploadImage(url : String , id: String, imageFile: File): Generic<LoginImage> {
          return try {
              val requestBodyId = id.toRequestBody("text/plain".toMediaTypeOrNull())
              val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
              val multipartBody = MultipartBody.Part.createFormData("upload_image", imageFile.name, requestFile)

              val response = apiService.uploadImage(url,requestBodyId, multipartBody)

              if (response.isSuccessful && response.body() != null) {
                  Generic.Success(response.body()!!)
              } else {
                  Generic.Error("Upload failed: ${response.code()} - ${response.message()}")
              }
          } catch (e: Exception) {
              Generic.Failure(e)
          }
      }*/


     */

    suspend fun uploadImage(url: String, id: String, imageFile: File): Generic<LoginImage> {
        return try {
            val requestBodyId = id.toRequestBody("text/plain".toMediaTypeOrNull())

            val requestFile =
                imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull()) // <- use jpeg for JPG files
            val multipartBody =
                MultipartBody.Part.createFormData("upload_image", imageFile.name, requestFile)

            val response = apiService.uploadImage(url, requestBodyId, multipartBody)

            if (response.isSuccessful && response.body() != null) {
                Generic.Success(response.body()!!)
            } else {
                Generic.Error("Upload failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Generic.Failure(e)
        }
    }


    suspend fun getEntertainment(url: String): Generic<EntertainmentResponse> {
        return try {
            val response = apiService.getEntertainment(url)
            if (response.isSuccessful && response.body() != null) {
                Log.e("entertainment request url", "" + response.raw().request)
                Log.e("entertainment response code", "" + response.code())
                Log.e("entertainment response body", "" + response.body())
                Generic.Success(response.body()!!)
            } else {
                Log.e("entertainment error", "" + response.message())
                Generic.Error("Server error: ${response.code()} - ${response.message()}")

            }
        } catch (e: Exception) {
            Log.e("entertainment failure exception", "" + e.message)
            Generic.Failure(e)
        }
    }


}