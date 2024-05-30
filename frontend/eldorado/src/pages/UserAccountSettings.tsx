import {SetStateAction, useEffect, useState} from 'react';
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Button} from "@/components/ui/button.tsx";
import {useForm} from "react-hook-form";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {isValidPhoneNumber} from "react-phone-number-input/min";
import {api} from "@/api/api.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useAccount} from "@/hooks/useAccount.ts";
import {useParams} from "react-router-dom";
import {AccountTypeEnum, localDateTimeToDate, UserType} from "@/types/Users.ts";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Loader2, Slash} from "lucide-react";
import EmailForm from "@/components/forms/EmailForm.tsx";
import PersonalInfoForm from "@/components/forms/PersonalInfoForm.tsx";
import DetailsForm from "@/components/forms/DetailsForm.tsx";
import UserLevelsForm from "@/components/forms/UserLevelsForm.tsx";
import UserHistoryPage from "@/pages/UserHistoryPage.tsx";

function UserAccountSettings() {
    const [activeForm, setActiveForm] = useState('Details');
    const [managedUser, setManagedUser] = useState<UserType | null>(null);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [levelToChange, setLevelToChange] = useState<AccountTypeEnum | null>(null);
    const {id} = useParams<{ id: string }>();
    const {t} = useTranslation();
    const [formValues, setFormValues] = useState(null);
    const [formType, setFormType] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [isLoadingButton, setIsLoadingButton] = useState(false);
    const [isRefreshing, setIsRefreshing] = useState(false);

    const emailSchema = z.object({
        email: z.string().email({message: t("accountSettings.wrongEmail")}),
    });

    const userDataSchema = z.object({
        name: z.string()
            .min(2, {message: t("accountSettings.firstNameTooShort")})
            .max(50, {message: t("accountSettings.firstNameTooLong")})
            .regex(/^[A-Za-z]+$/, {message: t("general.nameInvalid")})
            .optional(),
        lastName: z.string()
            .min(2, {message: t("accountSettings.lastNameTooShort")})
            .max(50, {message: t("accountSettings.lastNameTooLong")})
            .regex(/^[A-Za-z]+$/, {message: t("general.lastNameInvalid")})
            .optional(),
        phoneNumber: z.string().refine(isValidPhoneNumber, {message: t("accountSettings.phoneNumberInvalid")}).optional(),
        twoFactorAuth: z.boolean().optional().default(managedUser?.twoFactorAuth || false),
    });

    useEffect(() => {
        if (id) {
            api.getAccountById(id).then(response => {

                let creationDate = null;
                let lastSuccessfulLoginTime = null;
                let lastUnsuccessfulLoginTime = null;

                if (response.data.creationDate) {
                    creationDate = localDateTimeToDate(response.data.creationDate);
                }
                if (response.data.lastSuccessfulLoginTime) {
                    lastSuccessfulLoginTime = localDateTimeToDate(response.data.lastSuccessfulLoginTime);
                }

                if (response.data.lastUnsuccessfulLoginTime) {
                    lastUnsuccessfulLoginTime = localDateTimeToDate(response.data.lastUnsuccessfulLoginTime);
                }

                const managedUser: UserType = {
                    accountLanguage: response.data.accountLanguage,
                    active: response.data.active,
                    blocked: response.data.blocked,
                    email: response.data.email,
                    id: response.data.id,
                    lastname: response.data.lastname,
                    login: response.data.login,
                    name: response.data.name,
                    phoneNumber: response.data.phoneNumber,
                    userLevelsDto: response.data.userLevelsDto,
                    suspended: response.data.suspended,
                    version: response.data.version,
                    twoFactorAuth: response.data.twoFactorAuth,
                    lastSuccessfulLoginIp: response.data.lastSuccessfulLoginIp,
                    lastUnsuccessfulLoginIp: response.data.lastUnsuccessfulLoginIp,
                    creationDate: creationDate,
                    lastSuccessfulLoginTime: lastSuccessfulLoginTime,
                    lastUnsuccessfulLoginTime: lastUnsuccessfulLoginTime,
                    token: '',
                    activeUserLevel: null,
                };
                //the token and activeUserLevel are not needed in this context hence null is passed
                setManagedUser(managedUser);
                window.localStorage.setItem('etag', response.headers['etag']);
            });
        }
    }, [id]);

    const {getCurrentAccount} = useAccount();

    const handleDialogAction = () => {
        if (formType === 'email') {
            // @ts-expect-error - fix this
            SubmitEmail(formValues);
        } else if (formType === 'userData') {
            // @ts-expect-error - fix this
            SubmitUserData(formValues);
        } else {
            SubmitPassword();
        }
        setAlertDialogOpen(false);
    };

    const handleRemoveClick = (level: AccountTypeEnum) => {
        setLevelToChange(level);
        setAlertDialogOpen(true);
    };

    const handleAddClick = (level: AccountTypeEnum) => {
        setLevelToChange(level);
        setAlertDialogOpen(true);
    };

    const onSubmitEmail = (values: SetStateAction<null>) => {
        setFormValues(values);
        // @ts-expect-error - fix this
        setFormType('email');
        setAlertDialogOpen(true);
    };

    const onSubmitUserData = (values: SetStateAction<null>) => {
        setFormValues(values);
        //@ts-expect-error - fix this
        setFormType('userData');
        setAlertDialogOpen(true);
    };

    const onSubmitPassword = () => {
        setAlertDialogOpen(true);
    };

    const confirmChangeUserLevel = () => {
        if (levelToChange && managedUser) {
            setIsLoadingButton(true)
            if (managedUser?.userLevelsDto.some(userLevel => userLevel.roleName === levelToChange.toUpperCase())) {
                let apiCall;
                switch (levelToChange) {
                    case AccountTypeEnum.ADMIN:
                        apiCall = api.removeLevelAdmin(managedUser.id);
                        break;
                    case AccountTypeEnum.STAFF:
                        apiCall = api.removeLevelStaff(managedUser.id);
                        break;
                    case AccountTypeEnum.CLIENT:
                        apiCall = api.removeLevelClient(managedUser.id);
                        break;
                }
                apiCall.then(() => {
                    setAlertDialogOpen(false);
                    toast({
                        title: t("accountSettings.popUp.changeUserDataOK.title"),
                        description: t("accountSettings.popUp.changeUserDataOK.userLevelRemoved")
                    })
                }).catch((error) => {
                    handleApiError(error);
                }).finally(() => {
                    setIsLoadingButton(false);
                });
            } else {
                let apiCall;
                switch (levelToChange) {
                    case AccountTypeEnum.ADMIN:
                        apiCall = api.addLevelAdmin(managedUser.id);
                        break;
                    case AccountTypeEnum.STAFF:
                        apiCall = api.addLevelStaff(managedUser.id);
                        break;
                    case AccountTypeEnum.CLIENT:
                        apiCall = api.addLevelClient(managedUser.id);
                        break;
                }
                apiCall.then(() => {
                    setAlertDialogOpen(false);
                    toast({
                        title: t("accountSettings.popUp.changeUserDataOK.title"),
                        description: t("accountSettings.popUp.changeUserDataOK.userLevelAdded")
                    })
                }).catch((error) => {
                    handleApiError(error);
                }).finally(() => {
                    setIsLoadingButton(false);
                    setLevelToChange(null);
                });
            }
        }
    }

    const formEmail = useForm({
        resolver: zodResolver(emailSchema),
    });

    const formUserData = useForm({
        resolver: zodResolver(userDataSchema),
    });

    const SubmitEmail = (values: z.infer<typeof emailSchema>) => {
        if (managedUser) {
            setIsLoading(true)
            api.changeEmailUser(managedUser.id, values.email).then(() => {
                toast({
                    title: t("accountSettings.popUp.changeUserDataOK.title"),
                    description: t("accountSettings.popUp.changeEmailOK.text"),
                });
                if (managedUser?.id) {
                    api.getAccountById(managedUser?.id).then(response => {
                        setManagedUser(response.data);
                    });
                }
                setFormType(null);
                setFormValues(null);

            }).catch((error) => {
                handleApiError(error);
            }).finally(() => {
                setIsLoading(false);
            });
        }
    };

    const SubmitUserData = (values: z.infer<typeof userDataSchema>) => {
        setIsLoading(true)
        const etag = window.localStorage.getItem('etag');
        if (managedUser && managedUser.accountLanguage && etag !== null) {
            const name = values.name !== undefined ? values.name : managedUser.name;
            const lastName = values.lastName !== undefined ? values.lastName : managedUser.lastname;
            const phoneNumber = values.phoneNumber !== undefined ? values.phoneNumber : managedUser.phoneNumber;
            const twoFactorAuth = values.twoFactorAuth !== undefined ? values.twoFactorAuth : managedUser.twoFactorAuth;

            api.modifyAccountUser(managedUser.login, managedUser.version, managedUser.userLevelsDto,
                name, lastName, phoneNumber, twoFactorAuth, etag)
                .then(() => {
                    getCurrentAccount();
                    toast({
                        title: t("accountSettings.popUp.changeUserDataOK.title"),
                        description: t("accountSettings.popUp.changeOtherUserDataOK.text"),
                    });
                    setFormType(null);
                    setFormValues(null);

                }).catch((error) => {
                handleApiError(error);
            }).finally(() => {
                setIsLoading(false);
            });
        } else {
            console.log('Account or account language is not defined');
        }
    };

    const SubmitPassword = () => {
        if (id) {
            setIsLoading(true)
            api.resetPasswordByAdmin(id).then(() => {
                getCurrentAccount();
                toast({
                    title: t("accountSettings.popUp.changePasswordOK.title"),
                    description: t("accountSettings.popUp.changeOtherUsersPasswordOK.text"),
                });
                setFormType(null);
                setFormValues(null);

            }).catch((error) => {
                handleApiError(error);
            }).finally(() => {
                setIsLoading(false);
            });
        }

    };

    const refresh = () => {
        setIsRefreshing(true);
        if (id) {
            api.getAccountById(id).then(response => {
                setManagedUser(response.data);
                window.localStorage.setItem('etag', response.headers['etag']);
            });
        }
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    return (
        <div>
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/manage-users">{t("breadcrumb.manageUsers")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.userAccount")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button onClick={refresh} variant={"ghost"} className="w-auto" disabled={isRefreshing}>
                    {isRefreshing ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("general.refresh")
                    )}
                </Button>
            </div>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">{t("accountSettings.users.table.settings.account.title")}{managedUser?.login}</h1>
                </div>
                <div
                    className="mx-auto grid w-full max-w-6xl items-start gap-6 md:grid-cols-[180px_1fr] lg:grid-cols-[250px_1fr]">
                    <nav
                        className="grid gap-4 text-sm text-muted-foreground"
                    >
                        <Button variant={`${activeForm === 'Details' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('Details')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            {t("accountSettings.details")}
                        </Button>
                        <Button variant={`${activeForm === 'UserLevels' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('UserLevels')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            {t("accountSettings.users.table.settings.account.userLevels")}
                        </Button>
                        <Button variant={`${activeForm === 'Personal Info' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('Personal Info')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            {t("accountSettings.users.table.settings.account.personalInfo")}
                        </Button>
                        <Button variant={`${activeForm === 'E-Mail' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('E-Mail')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            {t("accountSettings.email")}
                        </Button>
                        <Button variant={`${activeForm === 'Password' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('Password')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            {t("accountSettings.password")}
                        </Button>
                        <Button variant={`${activeForm === 'History' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('History')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            History
                        </Button>
                    </nav>
                    <div className="grid gap-6">
                        {activeForm === 'UserLevels' && managedUser?.userLevelsDto && (
                            <div>
                                {activeForm === 'UserLevels' && (
                                    <UserLevelsForm managedUser={managedUser} handleRemoveClick={handleRemoveClick}
                                                    handleAddClick={handleAddClick}
                                                    isAlertDialogOpen={isAlertDialogOpen}
                                                    setAlertDialogOpen={setAlertDialogOpen}
                                                    confirmChangeUserLevel={confirmChangeUserLevel}
                                                    levelToChange={levelToChange} isLoading={isLoadingButton}/>
                                )}
                            </div>
                        )}
                        {activeForm === 'E-Mail' && (
                            <EmailForm formEmail={formEmail} onSubmitEmail={onSubmitEmail} isLoading={isLoading}
                                       account={managedUser} handleDialogAction={handleDialogAction}
                                       isAlertDialogOpen={isAlertDialogOpen}
                                       setAlertDialogOpen={setAlertDialogOpen} isLoadingReset={isLoading}
                                       showResendButton={false}
                            />
                        )}
                        {activeForm === 'Password' && (
                            <div>
                                <Card className="mx-10 w-auto">
                                    <CardContent className={"flex items-center justify-center pt-5"}>
                                        <Button onClick={onSubmitPassword} className="w-full pb-2"
                                                disabled={isLoading}>
                                            {isLoading ? (
                                                <>
                                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                                </>
                                            ) : (
                                                t("resetPasswordPage.title")
                                            )}
                                        </Button>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("resetPasswordPage.resetPassword")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                        {activeForm === 'Personal Info' && (
                            <PersonalInfoForm formUserData={formUserData} onSubmitUserData={onSubmitUserData}
                                              isLoading={isLoading} account={managedUser}
                                              handleDialogAction={handleDialogAction}
                                              isAlertDialogOpen={isAlertDialogOpen}
                                              setAlertDialogOpen={setAlertDialogOpen}/>
                        )}
                        {activeForm === 'Details' && (
                            <DetailsForm account={managedUser}/>
                        )}
                        {activeForm === 'History' && (
                            <UserHistoryPage userId={managedUser?.id}/>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}

export default UserAccountSettings;