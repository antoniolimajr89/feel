package br.com.caelum.feel.feedback.reports.application.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.caelum.feel.feedback.classification.CategoryInfoRepository;
import br.com.caelum.feel.feedback.classification.ChooseCategoryInfoForm;
import br.com.caelum.feel.feedback.classification.NewCategoryCommentForm;
import br.com.caelum.feel.feedback.companyteams.domain.repositories.Teams;
import br.com.caelum.feel.feedback.questions.domain.actions.SaveReportPerTeamAction;
import br.com.caelum.feel.feedback.questions.domain.models.FeedbackAnswer;
import br.com.caelum.feel.feedback.questions.domain.models.ReportPerTeamAnswer;
import br.com.caelum.feel.feedback.questions.domain.respositories.FeedbackAnswerRepository;
import br.com.caelum.feel.feedback.questions.domain.respositories.Questions;
import br.com.caelum.feel.feedback.questions.domain.respositories.ReportPerTeamAnswerRepository;
import br.com.caelum.feel.feedback.reports.application.forms.SearchRawAnswersForm;
import br.com.caelum.feel.feedback.reports.application.validators.UserIsLeaderOfTeamValidator;
import br.com.caelum.feel.feedback.reports.application.views.AllAnsewrs;
import br.com.caelum.feel.feedback.reports.application.views.ReportPerTeamAnswerTable;
import br.com.caelum.feel.feedback.security.AuthenticatedUser;
import br.com.caelum.feel.security.SystemUser;

@Controller
public class FeedbackReportsController {

	@Autowired
	private SaveReportPerTeamAction saveReportPerTeamAction;
	@Autowired
	private FeedbackAnswerRepository feedbackAnswerRepository;
	@Autowired
	private ReportPerTeamAnswerRepository reportPerTeamAnswerRepository;
	@Autowired
	private Questions questionRepository;
	@Autowired
	private Teams teamRepository;
	@Autowired
	private CategoryInfoRepository categoryInfoRepository;

	@Autowired
	private AuthenticatedUser authenticatedUser;
	
	@InitBinder("searchRawAnswersForm")
	public void init(WebDataBinder binder,@AuthenticationPrincipal SystemUser currentUser) {
		binder.addValidators(new UserIsLeaderOfTeamValidator(teamRepository,currentUser,authenticatedUser));
	}

	@GetMapping("/reports/feedbak/compare-number-answers")
	public String dashboardCompareAnswersPercent(Model model,
			@RequestParam("cycleId") Integer cycleId) {

		List<ReportPerTeamAnswer> answers = reportPerTeamAnswerRepository.listCurrentView(cycleId);
		model.addAttribute("answersList", new ReportPerTeamAnswerTable(answers));
		model.addAttribute("questionsList",
				questionRepository.findByCycleIdOrderByDueDateAsc(cycleId, PageRequest.of(0, 6)));
		model.addAttribute("teamsList", teamRepository.findAll());
		return "admin/reports/compare-teams";
	}

	@GetMapping("/reports/feedbak/compare-number-answers-values")
	public String dashboardCompareValuesPercent(Model model,
			@RequestParam("cycleId") Integer cycleId) {

		List<ReportPerTeamAnswer> answers = reportPerTeamAnswerRepository.listCurrentView(cycleId);
		model.addAttribute("answersList", new ReportPerTeamAnswerTable(answers));
		model.addAttribute("questionsList",
				questionRepository.findByCycleIdOrderByDueDateAsc(cycleId, PageRequest.of(0, 6)));
		model.addAttribute("teamsList", teamRepository.findAll());
		return "admin/reports/compare-answers-values";
	}

	@GetMapping("/reports/feedback/raw-answers/search")
	public String searchRawAnswers(Model model, @Valid SearchRawAnswersForm form,
			BindingResult result, @AuthenticationPrincipal SystemUser currentUser) {

		if (result.hasErrors()) {
			return rawAnswersList(model, form, currentUser);
		}
		
		List<FeedbackAnswer> answers = feedbackAnswerRepository.findAll(form.build());
		model.addAttribute("allAnswersPerTeamList", new AllAnsewrs(answers));
		return rawAnswersList(model, form, currentUser);
	}

	@GetMapping("/reports/feedback/raw-answers")
	public String rawAnswersList(Model model, SearchRawAnswersForm form,
			@AuthenticationPrincipal SystemUser currentUser) {
		model.addAttribute("questionList",
				questionRepository.findByCycleIdOrderByDueDateAsc(form.getCycleId()));
		
		model.addAttribute("categoryInfoList", categoryInfoRepository.findAll());
		
		if (authenticatedUser.isPeople(currentUser)) {
			model.addAttribute("teamList", teamRepository.findAll());
		} else {
			model.addAttribute("teamList",teamRepository.findByLeaderLogin(currentUser.getEmail()));
		}	

		if(!model.containsAttribute("newCategoryCommentForm")) {
			model.addAttribute("newCategoryCommentForm",new NewCategoryCommentForm(form));
		}
		
		if(!model.containsAttribute("chooseCategoryInfoForm")) {
			model.addAttribute("chooseCategoryInfoForm",new ChooseCategoryInfoForm(form));
		}
		
		//isso daqui é um comportamento não necessário, mas também não faz mal algum. Como esse método é chamado por outro controller,
		//resolvi deixar aqui. Espero achar um caminho melhor. 
		model.addAttribute("searchRawAnswersForm", form);
		
		return "admin/reports/raw-answers";
	}

	@PostMapping("/magic/kjfhsdjkfdsfsduhwied23/reports/feedback/views/per-team/{answerId}")
	public HttpEntity<?> saveRepostAnswerPerTeam(@PathVariable("answerId") Integer answerId) {
		saveReportPerTeamAction.execute(feedbackAnswerRepository.findById(answerId).get());
		return ResponseEntity.ok("");
	}
}
