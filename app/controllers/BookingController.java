package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Booking;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utilities.ActionAuthenticator;

/**
 * Created by yael on 10/13/15.
 */
public class BookingController extends Controller {

    @Transactional
    @Security.Authenticated(ActionAuthenticator.class)
    public Result create() {
        JsonNode json = request().body().asJson();
        Booking inputBooking = Json.fromJson(json, Booking.class);
        Long customerId = inputBooking.getCustomerId();
        if (customerId == null) {
            //TODO check customer exists in db
            Logger.error("controllers.BookingController.create(): Customer Id missing/invalid");
            return badRequest("A valid customer id is a mandatory field.");
        }

        Booking booking = new Booking(customerId);

        JPA.em().persist(booking);
        Logger.info("controllers.BookingController.create(): Created booking " + booking.toString());
        return ok(Json.toJson(booking.toString()));
    }

}