/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: Follower Entity
 *
 * Description:
 * This class represents a follower for a specified user. A follower is a user and thereby
 * extends the user class
 *
 */
package com.CMPUT301F21T19.habitappt.Entities;

public class Follower extends User {
    public Follower(String userEmail) {
        super(userEmail);
    }
}
