Feature:  Weather Forecast 

Scenario: As a choosey surfer 
	Given I like to surf in any 2 beaches Out of top ten of Sydney
	And I only like to surf on any 2 days specifically <Thursday & Friday> in next 16 Days
	When I look up the the weather forecast for the next 16 days using POSTAL CODES
	Then I check to if see the temperature is between "20" ℃ and "30" ℃ and UV index is <= "3"
	And I Pick two spots based on suitable weather forecast for the day

	
	

   