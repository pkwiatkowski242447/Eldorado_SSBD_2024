import {SetStateAction, useEffect, useState} from 'react';
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Button} from "@/components/ui/button.tsx";
import {useForm} from "react-hook-form";
import {isValidPhoneNumber} from "react-phone-number-input/min";
import {useAccountState} from "@/context/AccountContext.tsx";
import {api} from "@/api/api.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Slash} from "lucide-react";
import {RefreshButton} from "@/components/RefreshButton.tsx";
import PersonalInfoForm from "@/components/forms/PersonalInfoForm.tsx";
import PasswordForm from "@/components/forms/PasswordForm.tsx";
import EmailForm from "@/components/forms/EmailForm.tsx";
import DetailsForm from "@/components/forms/DetailsForm.tsx";
import UserHistoryPage from "@/pages/UserHistoryPage.tsx";
import AttributesPage from "@/pages/AttributesPage.tsx";
import {Pathnames} from "@/router/pathnames.ts";
import {useNavigate} from "react-router-dom";


function AccountSettings() {
    const [activeForm, setActiveForm] = useState('Details');
    const {account} = useAccountState();
    const {t} = useTranslation();
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [formValues, setFormValues] = useState(null);
    const [formType, setFormType] = useState(null);
    const {getCurrentAccount} = useAccount();
    const [isLoading, setIsLoading] = useState(false);
    const [isLoadingReset, setIsLoadingReset] = useState(false);
    const navigate = useNavigate();

    const emailSchema = z.object({
        email: z.string().email({message: t("accountSettings.wrongEmail")}),
    });

    const userDataSchema = z.object({
        name: z.string()
            .min(2, {message: t("accountSettings.firstNameTooShort")})
            .max(32, {message: t("accountSettings.firstNameTooLong")})
            .regex(/^[A-Za-z]+$/, {message: t("general.nameInvalid")})
            .optional(),
        lastName: z.string()
            .min(2, {message: t("accountSettings.lastNameTooShort")})
            .max(32, {message: t("accountSettings.lastNameTooLong")})
            .regex(/^[A-Za-z]+$/, {message: t("general.lastNameInvalid")})
            .optional(),
        phoneNumber: z.string().refine(isValidPhoneNumber, {message: t("accountSettings.phoneNumberInvalid")}).optional(),
        twoFactorAuth: z.boolean().optional().default(account?.twoFactorAuth || false),
    });

    const passwordSchema = z.object({
        oldPassword: z.string().min(8, {message: t("accountSettings.passwordTooShort")})
            .max(60, {message: t("accountSettings.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
        newPassword: z.string().min(8, {message: t("accountSettings.passwordTooShort")})
            .max(60, {message: t("accountSettings.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
        newPasswordRepeat: z.string().min(8, {message: t("accountSettings.passwordTooShort")})
            .max(60, {message: t("accountSettings.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
    }).refine(values => values.newPassword === values.newPasswordRepeat, {
        message: t("accountSettings.passwordsMustMatch"),
        path: ["newPasswordRepeat"],
    }).refine(values => values.oldPassword !== values.newPassword, {
        message: t("accountSettings.newPasswordCanNotBeTheSame"),
        path: ["newPassword"],
    });

    useEffect(() => {
        getCurrentAccount();
    }, []);

    const formEmail = useForm({
        resolver: zodResolver(emailSchema),
    });

    const formUserData = useForm({
        resolver: zodResolver(userDataSchema),
    });

    const formPassword = useForm({
        resolver: zodResolver(passwordSchema),
    });

    const onSubmitPassword = (values: z.infer<typeof passwordSchema>) => {
        // @ts-expect-error - fix this
        setFormValues(values);
        // @ts-expect-error - fix this
        setFormType('password');
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

    const handleDialogAction = () => {
        if (formType === 'email') {
            // @ts-expect-error - fix this
            SubmitEmail(formValues);
        } else if (formType === 'userData') {
            // @ts-expect-error - fix this
            SubmitUserData(formValues);
        } else if (formType === 'password') {
            // @ts-expect-error - fix this
            SubmitPassword(formValues);
        }
        setAlertDialogOpen(false);
    };

    const SubmitEmail = (values: z.infer<typeof emailSchema>) => {
        setIsLoading(true)
        api.changeEmailSelf(values.email).then(() => {
            getCurrentAccount();
            toast({
                title: t("accountSettings.popUp.changeEmailOK.title"),
                description: t("accountSettings.popUp.changeEmailOK.text"),
            });
            setFormType(null);
            setFormValues(null);

        }).catch((error) => {
            handleApiError(error);
        }).finally(() => {
            setIsLoading(false)
        });
    };

    const SubmitPassword = (values: z.infer<typeof passwordSchema>) => {
        setIsLoading(true)
        api.changePasswordSelf(values.oldPassword, values.newPassword).then(() => {
            getCurrentAccount();
            toast({
                title: t("accountSettings.popUp.changePasswordOK.title"),
                description: t("accountSettings.popUp.changePasswordOK.text"),
            });
            setFormType(null);
            setFormValues(null);

        }).catch((error) => {
            handleApiError(error);
        }).finally(() => {
            setIsLoading(false)
        });
    };

    const resendEmailConfirmation = () => {
        setIsLoadingReset(true)
        api.resendEmailConfirmation().then(() => {
            toast({
                title: t("accountSettings.popUp.resendEmailConfirmationOK.title"),
                description: t("accountSettings.popUp.resendEmailConfirmationOK.text"),
            });
        }).catch((error) => {
            handleApiError(error);
        }).finally(() => {
            setIsLoadingReset(false)
        });
    }

    const SubmitUserData = (values: z.infer<typeof userDataSchema>) => {
        setIsLoading(true)
        const etag = window.localStorage.getItem('etag');
        if (account && account.accountLanguage && etag !== null) {
            const name = values.name !== undefined ? values.name : account.name;
            const lastName = values.lastName !== undefined ? values.lastName : account.lastname;
            const phoneNumber = values.phoneNumber !== undefined ? values.phoneNumber : account.phoneNumber;
            const twoFactorAuth = values.twoFactorAuth !== undefined ? values.twoFactorAuth : account.twoFactorAuth;

            api.modifyAccountSelf(account.login, account.version, account.userLevelsDto,
                name, lastName, phoneNumber, twoFactorAuth, etag)
                .then(() => {
                    getCurrentAccount();
                    toast({
                        title: t("accountSettings.popUp.changeUserDataOK.title"),
                        description: t("accountSettings.popUp.changeUserDataOK.text"),
                    });
                    setFormType(null);
                    setFormValues(null);

                }).catch((error) => {
                handleApiError(error);
            }).finally(() => {
                setIsLoading(false)
            });
        } else {
            console.log('Account or account language is not defined');
        }
    };

    return (
        <div>
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className="pl-2">
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.loggedIn.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.myAccount")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <RefreshButton/>
            </div>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">{t("accountSettings.title")}</h1>
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
                        <Button variant={`${activeForm === 'Personal Info' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('Personal Info')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            {t("accountSettings.personalInfo")}
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
                            {t("accountSettings.history")}
                        </Button>
                        <Button variant={`${activeForm === 'Attributes' ? 'outline' : 'ghost'}`}
                                onClick={() => setActiveForm('Attributes')}
                                className={`text-muted-foreground transition-colors hover:text-foreground`}>
                            {t("accountSettings.attributes")}
                        </Button>
                    </nav>
                    <div className="grid gap-6">
                        {activeForm === 'E-Mail' && (
                            <EmailForm formEmail={formEmail} onSubmitEmail={onSubmitEmail} isLoading={isLoading}
                                       account={account} handleDialogAction={handleDialogAction}
                                       isAlertDialogOpen={isAlertDialogOpen}
                                       setAlertDialogOpen={setAlertDialogOpen} isLoadingReset={isLoadingReset}
                                       resendEmailConfirmation={resendEmailConfirmation} showResendButton={true}/>
                        )}
                        {activeForm === 'Password' && (
                            <PasswordForm formPassword={formPassword} onSubmitPassword={onSubmitPassword}
                                          isLoading={isLoading} setAlertDialogOpen={setAlertDialogOpen}
                                          handleDialogAction={handleDialogAction}
                                          isAlertDialogOpen={isAlertDialogOpen}/>
                        )}
                        {activeForm === 'Personal Info' && (
                            <PersonalInfoForm formUserData={formUserData} onSubmitUserData={onSubmitUserData}
                                              isLoading={isLoading} account={account}
                                              handleDialogAction={handleDialogAction}
                                              isAlertDialogOpen={isAlertDialogOpen}
                                              setAlertDialogOpen={setAlertDialogOpen}/>
                        )}
                        {activeForm === 'Details' && (
                            <DetailsForm account={account}/>
                        )}
                        {activeForm === 'History' && (
                            <UserHistoryPage/>
                        )}
                        {activeForm === 'Attributes' && (
                            <AttributesPage/>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}

export default AccountSettings;