# 1.  Record architecture decisions

Date: 2021-04-05

## Status

Accepted

## Context

The Vlingo Xoom provide to generate a project using annotation and auto dispatch.
using maven the auto dispatch work fine, but as we state to use gradle over maven as a build system,
we have some trouble with the auto dispatch: getting a Null Exception when run gradle build.

## Decision

As we are in the context of a challenge and don't have enough of time to try to find a solution to this problem.
we decided do work with a quick fix, use vlingo xoom annotation and disable the usage of aut dispatch.

## Consequences

All projects will use vlingo xoom annotation only.