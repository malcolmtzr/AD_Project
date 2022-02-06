package com.example.adproject.foodrec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.adproject.model.MealEntry;
import com.example.adproject.repo.MealEntryRepo;
import com.example.adproject.service.MealEntryService;

import lombok.Data;

@RestController
@RequestMapping("/api/recommend")
public class RecommenderController {
	private String[] titles = new String[5];
	
	@Autowired
	MealEntryRepo mrepo;
	
	@Autowired
	MealEntryService mService;
	
	SearchResult results = new SearchResult();
	
	String q = ""; 
	
//	@GetMapping("/getStringData")
//	public String getString() {
//		return "get text";
//	}
	
	
	@RequestMapping(value = "/postStringData", method = RequestMethod.POST)
	public String postString(@RequestParam String input, String track, String feeling) {
		System.out.println(input);
		System.out.println(track);
		System.out.println(feeling);
		q = input + " " + track + " " + feeling;
		System.out.println(q);
		return q;
	}
	
//	@RequestMapping(value = "/postpostStringData", method = RequestMethod.POST)
//	public String postpostString(@RequestBody String input) {
//		System.out.println(input);
//		return input;
//	}
	
	@GetMapping("/enterquery1")
	public String queryString() {
		return q;
	}
	
	@RequestMapping(value = "/runSearch", method = RequestMethod.POST)
	public SearchResult runSearch() {
		System.out.println("runSearch1");
		String url = "http://localhost:5000/enterquery";
		RestTemplate restTemplate = new RestTemplate();
		results = restTemplate.getForObject(url, SearchResult.class);
		System.out.println("got results from flask");
		System.out.println(results.getRes0());
		System.out.println(results.getRes1());
		System.out.println(results.getRes2());
		System.out.println(results.getRes3());
		System.out.println(results.getRes4());
		return results;
	}
	
//	@RequestMapping(value = "/postSearchResult", method = RequestMethod.POST)
//	public SearchResult postSearchResult(@RequestBody SearchResult result) {
//		results = result;
//		System.out.println(result.getRes0());
//		System.out.println(result.getRes1());
//		System.out.println(result.getRes2());
//		System.out.println(result.getRes3());
//		System.out.println(result.getRes4());
//		return result;
//	}
	
//	@GetMapping("/testResultEntry/{id}")
//	public String resultEntryString(@PathVariable Integer id) {
//		MealEntry m1 = mrepo.findById(1).get();
//		System.out.println(m1.getId());
//		System.out.println(m1.getTitle());
//		return m1.getTitle();
//	}
//	
//	@GetMapping("/testResultEntry2")
//	public String resultEntryString() {
//		MealEntry m1 = mrepo.findById(results.getRes0()).get();
//		System.out.println(m1.getId());
//		System.out.println(m1.getTitle());
//		return m1.getTitle();
//	}
	
	@GetMapping("/getResultJson")
	public ResultJson resultJson() {
		results = runSearch();
		ResultJson r = new ResultJson();
		System.out.println("getResultJson");
		System.out.println(results.getRes0());
		System.out.println(results.getRes1());
		System.out.println(results.getRes2());
		System.out.println(results.getRes3());
		System.out.println(results.getRes4());
		titles[0] = mrepo.findById(results.getRes0()).get().getTitle();
		titles[1] = mrepo.findById(results.getRes1()).get().getTitle();
		titles[2] = mrepo.findById(results.getRes2()).get().getTitle();
		titles[3] = mrepo.findById(results.getRes3()).get().getTitle();
		titles[4] = mrepo.findById(results.getRes4()).get().getTitle();
		System.out.println("In getResultJson");
		for (String s : titles) {
			System.out.println(s);
		}
		r.setTitles(titles);
		return r;
	}
	
	//pass mealEntryTable to Flask
	@RequestMapping(value = "/passData", method = {RequestMethod.POST, RequestMethod.GET})
	public EntriesDataTable passDataToFlask() {
		List<MealEntry> entries = mService.findMealEntryByUserId(1);
		Integer size = entries.size();
		EntriesDataTable data = new EntriesDataTable();
		List<Integer> ids = new ArrayList<Integer>();
		List<String> titles = new ArrayList<String>();
		List<String> descriptions = new ArrayList<String>();
		List<String> feelings = new ArrayList<String>();
		List<Integer> track_scores = new ArrayList<Integer>();
		for (MealEntry m : entries) {
			ids.add(m.getId());
			titles.add(m.getTitle());
			descriptions.add(m.getDescription());
			feelings.add(m.getFeeling().toString());
			track_scores.add(m.getTrackScore());
		}
		data.setId(ids.toArray(new Integer[size]));
		data.setTitle(titles.toArray(new String[size]));
		data.setDescription(descriptions.toArray(new String[size]));
		data.setFeeling(feelings.toArray(new String[size]));
		data.setTrack_score(track_scores.toArray(new Integer[size]));
		return data;
	}
}
