// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.*;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Finds time ranges of events, that at least one attendee 
    // of the requested meeting attends those events.
    ArrayList<TimeRange> conflictTimeRanges = new ArrayList<TimeRange>();
    for (Event event : events) {
      if (conflict(event.getAttendees(), request.getAttendees())) {
        conflictTimeRanges.add(event.getWhen());
      }
    }

    if (conflictTimeRanges.size() == 0) {
      if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
        return Arrays.asList();
      }
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    // Sorts ranges by their start time in ascending order.
    Collections.sort(conflictTimeRanges, TimeRange.ORDER_BY_START);

    // Merges events in conflictTimeRanges.
    ArrayList<TimeRange> mergedTimeRanges = new ArrayList<TimeRange>();
    TimeRange current = conflictTimeRanges.get(0);
    for (TimeRange range : conflictTimeRanges) {
      if (range.overlaps(current)) {
        current = TimeRange.fromStartEnd(current.start(), Math.max(current.end(), range.end()), false);
      }else {
        mergedTimeRanges.add(current);
        current = range;
      }
    }
    mergedTimeRanges.add(current);

    // Finds time ranges that can hold the requested meeting.
    ArrayList<TimeRange> meetingTimeRanges = new ArrayList<TimeRange>();
    int start = TimeRange.START_OF_DAY;
    for (TimeRange range : mergedTimeRanges) {
      TimeRange freeTimeRange = TimeRange.fromStartEnd(start, range.start(), false);
      if (freeTimeRange.duration() >= request.getDuration()) {
        meetingTimeRanges.add(freeTimeRange);
      }
      start = range.end();
    }
    // Checks the last free time range of the day.
    TimeRange lastTimeRange = TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true);
    if (lastTimeRange.duration() >= request.getDuration()) {
      meetingTimeRanges.add(lastTimeRange);
    }

    return meetingTimeRanges;
  }

  // Checks if there is a same attendee in two events.
  private boolean conflict(Collection<String> attendeesA, Collection<String> attendeesB) {
    for (String a : attendeesA) {
      if (attendeesB.contains(a)) {
        return true;
      }
    }
    return false;
  }
}
