/**
 * Copyright (C) 2011  jtalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */


package org.jtalks.jcommune.web.controller;

import org.jtalks.jcommune.model.entity.Branch;
import org.jtalks.jcommune.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/** This controller displays all topic's branches and populates page with them
 * @author Vitaliy kravchenko
 */

@Controller
public final class BranchController {

    private BranchService branchService;

    /**
     * Constructor creates MVC controller with specified BranchService
     * @param branchService {@link org.jtalks.jcommune.service.BranchService} autowired object from Spring
     * Context
     */
    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * Populates page with a list of existing branches
     * @return list of {@link org.jtalks.jcommune.model.entity.Branch}, if no branches created it will return
     * empty list
     */
    @ModelAttribute("topicsBranchList")
    public List<Branch> populateFormWithBranches() {
        return branchService.getAll();
    }

    /**
     * This method handles GET request and produces JSP page with all topic branches
     * @return {@link ModelAndView} with view name as renderAllBranches
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView displayAllTopicsBranches() {
        return new ModelAndView("renderAllBranches");
    }

}