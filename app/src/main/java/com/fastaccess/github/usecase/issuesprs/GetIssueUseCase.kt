package com.fastaccess.github.usecase.issuesprs

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import com.fastaccess.data.model.CountModel
import com.fastaccess.data.model.EmbeddedRepoModel
import com.fastaccess.data.model.ReactionGroupModel
import com.fastaccess.data.persistence.models.IssueModel
import com.fastaccess.data.repository.IssueRepositoryProvider
import com.fastaccess.domain.usecase.base.BaseCompletableUseCase
import github.GetIssueQuery
import io.reactivex.Completable
import javax.inject.Inject

/**
 * Created by Kosh on 27.01.19.
 */
class GetIssueUseCase @Inject constructor(
    private val issueRepositoryProvider: IssueRepositoryProvider,
    private val apolloClient: ApolloClient
) : BaseCompletableUseCase() {

    var login: String? = null
    var repo: String? = null
    var number: Int? = null

    override fun buildCompletable(): Completable {
        val login = login
        val repo = repo
        val number = number

        if (login.isNullOrEmpty() || repo.isNullOrEmpty() || number == null) {
            return Completable.error(Throwable("this should never happen ;)"))
        }

        return Rx2Apollo.from(apolloClient.query(GetIssueQuery(login, repo, number)))
            .map { it.data()?.user?.repository?.issue }
            .map { issue ->
                return@map IssueModel(issue.id, issue.databaseId, issue.number, issue.activeLockReason?.rawValue(),
                    issue.body, issue.bodyHTML.toString(), issue.closedAt, issue.createdAt, issue.updatedAt, issue.state.rawValue(),
                    issue.title, issue.viewerSubscription?.rawValue(), EmbeddedRepoModel(null),
                    CountModel(issue.userContentEdits?.totalCount), issue.reactionGroups
                    ?.map { ReactionGroupModel(it.content.rawValue(), it.createdAt, CountModel(it.users.totalCount), it.isViewerHasReacted) },
                    issue.viewerCannotUpdateReasons.map { it.rawValue() }, issue.isClosed, issue.isCreatedViaEmail, issue.isLocked,
                    issue.isViewerCanReact, issue.isViewerCanSubscribe, issue.isViewerCanUpdate, issue.isViewerDidAuthor)
            }
            .flatMapCompletable { Completable.fromCallable { issueRepositoryProvider.upsert(it) } }
    }
}