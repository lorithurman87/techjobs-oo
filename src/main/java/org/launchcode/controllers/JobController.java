package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    @NotNull
    @Size(min=1, message = "Name may not be empty")
    private String name;

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job job = jobData.findById(id);
        model.addAttribute("job", job);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()){
            model.addAttribute(errors);
            return "new-job";
        }

        String newJobName = jobForm.getName();
        Employer newJobEmployer = jobData.getEmployers().findById(jobForm.getEmployerId());
        Location newJobLocation = jobData.getLocations().findById(jobForm.getLocationId());
        PositionType newJobPositionType = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());
        CoreCompetency newJobCoreCompetency = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetenciesId());
        Job newJob = new Job (newJobName, newJobEmployer, newJobLocation, newJobPositionType, newJobCoreCompetency);
        jobData.add(newJob);

        return "redirect:/job?id=" + newJob.getId();

    }
}
