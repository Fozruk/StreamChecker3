package com.github.fozruk.streamcheckerguitest.chat.hitbox;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * Created by Philipp on 13.07.2016.
 */
public class HitboxImplTest {

    @Test
    public void testChat() throws IOException, JSONException, URISyntaxException {
        HitboxImpl imp = new HitboxImpl(new URI("e"));
    }

}